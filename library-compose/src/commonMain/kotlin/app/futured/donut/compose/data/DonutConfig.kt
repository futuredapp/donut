package app.futured.donut.compose.data

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import app.futured.donut.compose.DonutProgress

/**
 * Configuration class for [DonutProgress] animations.
 *
 * @param gapAngleAnimationSpec animation spec for gap angle animation
 * @param masterProgressAnimationSpec animation spec for master progress animation
 * @param gapWidthAnimationSpec animation spec for gap width animation
 * @param strokeWidthAnimationSpec animation spec for stroke width animation
 * @param backgroundLineColorAnimationSpec animation spec for background line color change animation
 * @param capAnimationSpec animation spec for cap animation
 * @param sectionAmountAnimationSpec animation spec for entries progress animations
 * @param sectionColorAnimationSpec animation spec for entries color change animations
 */
data class DonutConfig(
    val gapAngleAnimationSpec: AnimationSpec<Float>,
    val masterProgressAnimationSpec: AnimationSpec<Float>,
    val gapWidthAnimationSpec: AnimationSpec<Float>,
    val strokeWidthAnimationSpec: AnimationSpec<Float>,
    val backgroundLineColorAnimationSpec: AnimationSpec<Color>,
    val capAnimationSpec: AnimationSpec<Float>,
    val sectionAmountAnimationSpec: AnimationSpec<Float>,
    val sectionColorAnimationSpec: AnimationSpec<Color>
) {

    companion object {

        /**
         * The factory method for [DonutConfig] that simplifies the creation of new configuration instances
         * where all layout animations or all color animations should behave similarly
         * (Eg. all layout animations will use the same animation spec).
         *
         * @param layoutAnimationSpec animation builder for all layout animations
         * @param colorAnimationSpec animation builder for all color change animations
         * @return the new instance of [DonutConfig]
         */
        fun create(
            layoutAnimationSpec: AnimationSpec<Float> = tween(),
            colorAnimationSpec: AnimationSpec<Color> = tween()
        ): DonutConfig {
            return DonutConfig(
                gapAngleAnimationSpec = layoutAnimationSpec,
                masterProgressAnimationSpec = layoutAnimationSpec,
                gapWidthAnimationSpec = layoutAnimationSpec,
                strokeWidthAnimationSpec = layoutAnimationSpec,
                backgroundLineColorAnimationSpec = colorAnimationSpec,
                capAnimationSpec = layoutAnimationSpec,
                sectionAmountAnimationSpec = layoutAnimationSpec,
                sectionColorAnimationSpec = colorAnimationSpec
            )
        }
    }

    /**
     * Returns new instance of [DonutConfig] with all layout animation specs set to provided [animationSpec].
     *
     * @param animationSpec layout animation spec
     * @return the new instance of [DonutConfig]
     */
    fun copyWithLayoutAnimationsSpec(animationSpec: AnimationSpec<Float>) = copy(
        gapAngleAnimationSpec = animationSpec,
        masterProgressAnimationSpec = animationSpec,
        gapWidthAnimationSpec = animationSpec,
        strokeWidthAnimationSpec = animationSpec,
        capAnimationSpec = animationSpec,
        sectionAmountAnimationSpec = animationSpec
    )

    /**
     * Returns new instance of [DonutConfig] with all color animation specs set to provided [animationSpec].
     *
     * @param animationSpec color animation spec
     * @return the new instance of [DonutConfig]
     */
    fun copyWithColorAnimationsSpec(animationSpec: AnimationSpec<Color>) = copy(
        backgroundLineColorAnimationSpec = animationSpec,
        sectionColorAnimationSpec = animationSpec
    )
}
