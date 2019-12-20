package app.futured.donut.extensions

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the collection.
 */
internal inline fun <T> Iterable<T>.sumByFloat(selector: (T) -> Float): Float {
    var sum: Float = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

internal inline fun <T, R> Iterable<T>.hasDuplicatesBy(selector: (T) -> R): Boolean {
    val registry = mutableListOf<R>()
    forEach {
        val sel = selector(it)
        if (registry.contains(sel)) {
            return true
        }
        registry.add(sel)
    }
    return false
}
