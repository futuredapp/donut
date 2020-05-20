package app.futured.donut.compose.data

import androidx.animation.AnimationBuilder
import androidx.animation.TweenBuilder
import androidx.compose.Model
import androidx.ui.graphics.Color
import app.futured.donut.compose.DonutProgress

/**
 * Configuration class for [DonutProgress]
 *
 * @param isGapAngleAnimationEnabled enabled or disable gap angle animation
 * @param gapAngleAnimationBuilder animation builder for gap angle animation
 * @param isMasterProgressAnimationEnabled enabled or disable master progress animation
 * @param masterProgressAnimationBuilder animation builder for master progress animation
 * @param isGapWidthAnimationEnabled enabled or disable gap width animation
 * @param gapWidthAnimationBuilder animation builder for gap width animation
 * @param isStrokeWidthAnimationEnabled enabled or disable stroke width animation
 * @param strokeWidthAnimationBuilder animation builder for stroke width animation
 * @param isBackgroundLineColorAnimationEnabled enabled or disable background line color change animation
 * @param backgroundLineColorAnimationBuilder animation builder for background line color change animation
 * @param isCapAnimationEnabled enabled or disable cap animation
 * @param capAnimationBuilder animation builder for cap animation
 * @param isSectionAmountAnimationEnabled enabled or disable entries progress animations
 * @param sectionAmountAnimationBuilder animation builder for entries progress animations
 * @param isSectionColorAnimationEnabled enabled or disable entries color change animations
 * @param sectionColorAnimationBuilder animation builder for entries color change animations
 */
@Model data class DonutConfig(
    var isGapAngleAnimationEnabled: Boolean = true,
    var gapAngleAnimationBuilder: AnimationBuilder<Float> = getDefaultFloatAnimationBuilder(),
    var isMasterProgressAnimationEnabled: Boolean = true,
    var masterProgressAnimationBuilder: AnimationBuilder<Float> = getDefaultFloatAnimationBuilder(),
    var isGapWidthAnimationEnabled: Boolean = true,
    var gapWidthAnimationBuilder: AnimationBuilder<Float> = getDefaultFloatAnimationBuilder(),
    var isStrokeWidthAnimationEnabled: Boolean = true,
    var strokeWidthAnimationBuilder: AnimationBuilder<Float> = getDefaultFloatAnimationBuilder(),
    var isBackgroundLineColorAnimationEnabled: Boolean = true,
    var backgroundLineColorAnimationBuilder: AnimationBuilder<Color> = getDefaultColorAnimationBuilder(),
    var isCapAnimationEnabled: Boolean = true,
    var capAnimationBuilder: AnimationBuilder<Float> = getDefaultFloatAnimationBuilder(),
    var isSectionAmountAnimationEnabled: Boolean = true,
    var sectionAmountAnimationBuilder: AnimationBuilder<Float> = getDefaultFloatAnimationBuilder(),
    var isSectionColorAnimationEnabled: Boolean = true,
    var sectionColorAnimationBuilder: AnimationBuilder<Color> = getDefaultColorAnimationBuilder()
) {

    companion object {

        /**
         * The factory method for [DonutConfig] that simplifies the creation of new configuration instances where all layout
         * animations or all color animations should behave similarly (Eg. all layout animations will be enabled and will use
         * the same animation builder).
         *
         * @param isLayoutAnimationsEnabled enable or disable all layout animations
         * @param isColorAnimationEnabled enable or disable all color change animations
         * @param layoutAnimationBuilder animation builder for all layout animations
         * @param colorAnimationBuilder animation builder for all color change animations
         * @return the new instance of [DonutConfig]
         */
        fun create(
            isLayoutAnimationsEnabled: Boolean = true,
            isColorAnimationEnabled: Boolean = true,
            layoutAnimationBuilder: AnimationBuilder<Float> = getDefaultFloatAnimationBuilder(),
            colorAnimationBuilder: AnimationBuilder<Color> = getDefaultColorAnimationBuilder()
        ): DonutConfig {
            return DonutConfig(
                isGapAngleAnimationEnabled = isLayoutAnimationsEnabled,
                gapAngleAnimationBuilder = layoutAnimationBuilder,
                isMasterProgressAnimationEnabled = isLayoutAnimationsEnabled,
                masterProgressAnimationBuilder = layoutAnimationBuilder,
                isGapWidthAnimationEnabled = isLayoutAnimationsEnabled,
                gapWidthAnimationBuilder = layoutAnimationBuilder,
                isStrokeWidthAnimationEnabled = isLayoutAnimationsEnabled,
                strokeWidthAnimationBuilder = layoutAnimationBuilder,
                isBackgroundLineColorAnimationEnabled = isColorAnimationEnabled,
                backgroundLineColorAnimationBuilder = colorAnimationBuilder,
                isCapAnimationEnabled = isLayoutAnimationsEnabled,
                capAnimationBuilder = layoutAnimationBuilder,
                isSectionAmountAnimationEnabled = isLayoutAnimationsEnabled,
                sectionAmountAnimationBuilder = layoutAnimationBuilder,
                isSectionColorAnimationEnabled = isColorAnimationEnabled,
                sectionColorAnimationBuilder = colorAnimationBuilder
            )
        }
    }

    /**
     * Enable or disable all animations
     *
     * @param isEnabled
     */
    fun setAnimationsEnabled(isEnabled: Boolean) {
        setLayoutAnimationsEnabled(isEnabled)
        setColorAnimationsEnabled(isEnabled)
    }

    /**
     * Enable or disable all layout change animations
     *
     * @param isEnabled
     */
    fun setLayoutAnimationsEnabled(isEnabled: Boolean) {
        isGapAngleAnimationEnabled = isEnabled
        isMasterProgressAnimationEnabled = isEnabled
        isGapWidthAnimationEnabled = isEnabled
        isStrokeWidthAnimationEnabled = isEnabled
        isCapAnimationEnabled = isEnabled
        isSectionAmountAnimationEnabled = isEnabled
    }

    /**
     * Enable or disable all layout color change animations
     *
     * @param isEnabled
     */
    fun setColorAnimationsEnabled(isEnabled: Boolean) {
        isBackgroundLineColorAnimationEnabled = isEnabled
        isSectionColorAnimationEnabled = isEnabled
    }

    /**
     * Set animation builder for all layout animations
     *
     * @param builder
     */
    fun setLayoutAnimationBuilder(builder: AnimationBuilder<Float>) {
        gapAngleAnimationBuilder = builder
        masterProgressAnimationBuilder = builder
        gapWidthAnimationBuilder = builder
        strokeWidthAnimationBuilder = builder
        capAnimationBuilder = builder
        sectionAmountAnimationBuilder = builder
    }

    /**
     * Set animation builder for all color change animations
     *
     * @param builder
     */
    fun setColorAnimationBuilder(builder: AnimationBuilder<Color>) {
        backgroundLineColorAnimationBuilder = builder
        sectionColorAnimationBuilder = builder
    }
}

private fun getDefaultFloatAnimationBuilder() = TweenBuilder<Float>()

private fun getDefaultColorAnimationBuilder() = TweenBuilder<Color>()
