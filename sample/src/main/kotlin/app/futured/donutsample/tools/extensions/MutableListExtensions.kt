package app.futured.donutsample.tools.extensions

fun <T : Any> MutableList<T>.modifyAt(index: Int, modifier: (T) -> T) {
    this[index] = modifier(this[index])
}
