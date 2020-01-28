package app.futured.donutsample.tools.extensions

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the collection.
 */
inline fun <T> Iterable<T>.sumByFloat(selector: (T) -> Float): Float {
    var sum: Float = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}
