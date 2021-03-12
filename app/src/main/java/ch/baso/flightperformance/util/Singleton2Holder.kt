package ch.baso.flightperformance.util

open class Singleton2Holder<out T, in A, B>(private val constructor: (A, B) -> T) {

    @Volatile
    private var instance: T? = null

    fun init(arg: A, arg2: B): T {
        return when {
            instance != null -> instance!!
            else -> synchronized(this) {
                if (instance == null) instance = constructor(arg, arg2)
                instance!!
            }
        }
    }
    fun getInstance(): T {
        return instance!!
    }
}