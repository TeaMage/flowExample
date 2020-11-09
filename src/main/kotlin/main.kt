import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// flow example
fun numbers(): Flow<Int> = flow {
    (1..3).forEach {
        delay(100)
        println("emitting value")
        emit(it)
    }
}.flowOn((Dispatchers.Default))

// simulates api request
suspend fun performRequest(request: Int): String {
    delay(1000)
    return "response $request"
}

// run the flow on Dispatchers.Default Coroutine
// collect it in the main Coroutine
fun main() = runBlocking<Unit> {
    numbers()
        .onCompletion { println("Flow completed") }
        .map { performRequest(it) }
        .onEach { value -> check(value != "response 3") { "Collected $value" } }
        .catch { e -> println("Caught $e") }
        .collect { value -> println(value) }
    println("Main finished")
}