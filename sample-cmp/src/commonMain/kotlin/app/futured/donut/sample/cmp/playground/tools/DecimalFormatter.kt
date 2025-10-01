package app.futured.donut.sample.cmp.playground.tools

import kotlin.math.pow

/**
 * Formats a [Float] to a [String] with a specified number of decimal places.
 *
 * @param decimals The number of decimal places to round to. Defaults to 2.
 * @return A [String] representation of the [Float] rounded to the specified number of decimal places.
 */
fun Float.formatDecimals(decimals: Int = 2): String {
    val factor = 10.0.pow(decimals).toFloat()
    val rounded = kotlin.math.round(this * factor) / factor
    return buildString {
        append(rounded)
    }
}

