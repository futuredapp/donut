package app.futured.donut.compose.internal.extensions

import androidx.animation.AnimationBuilder
import androidx.animation.BaseAnimatedValue

internal fun <VALUE> BaseAnimatedValue<VALUE, *>.animateOrSnapDistinctValues(
    newValue: VALUE,
    isAnimationEnabled: Boolean,
    animationBuilder: AnimationBuilder<VALUE>
) {
    if (newValue != targetValue) {
        if (isAnimationEnabled) {
            animateTo(newValue, animationBuilder)
        } else {
            snapTo(newValue)
        }
    }
}
