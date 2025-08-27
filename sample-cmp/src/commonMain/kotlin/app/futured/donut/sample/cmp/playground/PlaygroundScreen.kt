package app.futured.donut.sample.cmp.playground

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import app.futured.donut.compose.data.DonutConfig
import app.futured.donut.compose.data.DonutModel
import app.futured.donut.compose.data.DonutSection
import app.futured.donut.sample.cmp.playground.PlaygroundScreenData.ColorAnimationSpecs
import app.futured.donut.sample.cmp.playground.PlaygroundScreenData.Companion.getColorAnimationSpec
import app.futured.donut.sample.cmp.playground.PlaygroundScreenData.Companion.getLayoutAnimationSpec
import app.futured.donut.sample.cmp.playground.PlaygroundScreenData.LayoutAnimationSpecs
import app.futured.donut.sample.cmp.playground.tools.RadioButtonGroup
import app.futured.donut.sample.cmp.playground.tools.SliderControl
import app.futured.donut.sample.cmp.playground.tools.formatDecimals
import donut.sample_cmp.generated.resources.Res
import donut.sample_cmp.generated.resources.amount_cap
import donut.sample_cmp.generated.resources.animation_duration
import donut.sample_cmp.generated.resources.animations
import donut.sample_cmp.generated.resources.button_add
import donut.sample_cmp.generated.resources.button_clear
import donut.sample_cmp.generated.resources.button_random_colors
import donut.sample_cmp.generated.resources.button_random_values
import donut.sample_cmp.generated.resources.button_remove
import donut.sample_cmp.generated.resources.color_animation_type
import donut.sample_cmp.generated.resources.data_manipulation
import donut.sample_cmp.generated.resources.gap_angle
import donut.sample_cmp.generated.resources.gap_width
import donut.sample_cmp.generated.resources.layout_animation_type
import donut.sample_cmp.generated.resources.master_progress
import donut.sample_cmp.generated.resources.seekbars
import donut.sample_cmp.generated.resources.stroke_cap_butt
import donut.sample_cmp.generated.resources.stroke_cap_round
import donut.sample_cmp.generated.resources.stroke_caps
import donut.sample_cmp.generated.resources.stroke_width
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random

interface IPlaygroundScreenData {
    val data: StateFlow<DonutModel>
    val config: StateFlow<DonutConfig>
    fun initExampleDataWithDelay(coroutineScope: CoroutineScope)
    fun mutateData(mapper: (data: DonutModel) -> DonutModel)
    fun mutateConfig(mapper: (config: DonutConfig) -> DonutConfig)
}

internal class PlaygroundScreenData : IPlaygroundScreenData {

    enum class LayoutAnimationSpecs {
        SNAP, LINEAR, SPRING, FAST_OUT_SLOW_IN, LINEAR_OUT_SLOW_IN, FAST_OUT_LINEAR_IN
    }

    enum class ColorAnimationSpecs {
        SNAP, LINEAR, FAST_OUT_SLOW_IN, LINEAR_OUT_SLOW_IN, FAST_OUT_LINEAR_IN
    }

    companion object {
        const val DATA_DELAY = 600L
        const val INITIAL_CAP = 8f
        const val INITIAL_MASTER_PROGRESS = 0f
        const val INITIAL_GAP_WIDTH = 90f
        const val INITIAL_GAP_ANGLE = 270f
        const val INITIAL_STROKE_WIDTH = 40f
        const val INITIAL_ANIMATION_DURATION = 1000
        val INITIAL_DONUT_DATA = DonutModel(
            cap = INITIAL_CAP,
            masterProgress = INITIAL_MASTER_PROGRESS,
            gapWidthDegrees = INITIAL_GAP_WIDTH,
            gapAngleDegrees = INITIAL_GAP_ANGLE,
            strokeWidth = INITIAL_STROKE_WIDTH,
            backgroundLineColor = Color(0xFFE7E8E9),
            sections = listOf(
                DonutSection(amount = 0f, color = Color(0xFF222222)),
                DonutSection(amount = 0f, color = Color(0xFF19D3C5)),
                DonutSection(amount = 0f, color = Color(0xFFFF5F00)),
                DonutSection(amount = 0f, color = Color(0xFF005FCC)),
            ),
        )
        val INITIAL_DONUT_CONFIG = DonutConfig.create(
            layoutAnimationSpec = tween(
                durationMillis = INITIAL_ANIMATION_DURATION,
                easing = CubicBezierEasing(0.18f, 0.7f, 0.16f, 1f),
            ),
            colorAnimationSpec = tween(durationMillis = INITIAL_ANIMATION_DURATION),
        )

        fun LayoutAnimationSpecs.getLayoutAnimationSpec(animDurationMs: Int): AnimationSpec<Float> {
            return when (this) {
                LayoutAnimationSpecs.SNAP -> snap()
                LayoutAnimationSpecs.LINEAR -> tween(animDurationMs, easing = LinearEasing)
                LayoutAnimationSpecs.SPRING -> spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
                LayoutAnimationSpecs.FAST_OUT_SLOW_IN -> tween(animDurationMs, easing = FastOutSlowInEasing)
                LayoutAnimationSpecs.LINEAR_OUT_SLOW_IN -> tween(animDurationMs, easing = LinearOutSlowInEasing)
                LayoutAnimationSpecs.FAST_OUT_LINEAR_IN -> tween(animDurationMs, easing = FastOutLinearInEasing)
            }
        }

        fun ColorAnimationSpecs.getColorAnimationSpec(animDurationMs: Int): AnimationSpec<Color> {
            return when (this) {
                ColorAnimationSpecs.SNAP -> snap()
                ColorAnimationSpecs.LINEAR -> tween(animDurationMs, easing = LinearEasing)
                ColorAnimationSpecs.FAST_OUT_SLOW_IN -> tween(animDurationMs, easing = FastOutSlowInEasing)
                ColorAnimationSpecs.LINEAR_OUT_SLOW_IN -> tween(animDurationMs, easing = LinearOutSlowInEasing)
                ColorAnimationSpecs.FAST_OUT_LINEAR_IN -> tween(animDurationMs, easing = FastOutLinearInEasing)
            }
        }
    }

    override val data = MutableStateFlow(INITIAL_DONUT_DATA)
    override val config = MutableStateFlow(INITIAL_DONUT_CONFIG)

    override fun initExampleDataWithDelay(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            delay(DATA_DELAY)
            mutateData { data ->
                data.copy(
                    masterProgress = 1f,
                    sections = listOf(
                        DonutSection(amount = 0.5f, color = Color(0xFF222222)),
                        DonutSection(amount = 1f, color = Color(0xFF19D3C5)),
                        DonutSection(amount = 1.5f, color = Color(0xFFFF5F00)),
                        DonutSection(amount = 2f, color = Color(0xFF005FCC)),
                    )
                )
            }
        }
    }

    override fun mutateData(mapper: (data: DonutModel) -> DonutModel) {
        data.value = mapper(data.value)
    }

    override fun mutateConfig(mapper: (config: DonutConfig) -> DonutConfig) {
        config.value = mapper(config.value)
    }
}

@Composable
fun PlaygroundScreen(modifier: Modifier = Modifier) {
    val dataProvider = remember { PlaygroundScreenData() }
    val data = dataProvider.data.collectAsState()
    val config = dataProvider.config.collectAsState()

    LaunchedEffect(Unit) {
        dataProvider.initExampleDataWithDelay(this)
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SampleDonutComponent(
            modifier = Modifier.padding(vertical = 8.dp),
            model = data.value,
            config = config.value,
        )

        Controls(dataProvider = dataProvider)
    }
}

@Composable
private fun Controls(
    modifier: Modifier = Modifier,
    dataProvider: IPlaygroundScreenData,
) {
    val data = dataProvider.data.collectAsState()

    val masterProgressValue by remember {
        derivedStateOf { data.value.masterProgress * 100f }
    }
    val gapWidthValue by remember {
        derivedStateOf { data.value.gapWidthDegrees }
    }
    val gapAngleValue by remember {
        derivedStateOf { data.value.gapAngleDegrees }
    }
    val strokeWidthValue by remember {
        derivedStateOf { data.value.strokeWidth }
    }
    val capValue by remember {
        derivedStateOf { data.value.cap }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Section: Seekbars
        Text(
            text = stringResource(Res.string.seekbars),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        // Master progress
        SliderControl(
            title = stringResource(Res.string.master_progress, masterProgressValue.toInt()),
            value = masterProgressValue,
            onValueChange = { newValue ->
                dataProvider.mutateData { data -> data.copy(masterProgress = newValue / 100f) }
            },
            valueRange = 0f..100f
        )

        // Gap width
        SliderControl(
            title = stringResource(Res.string.gap_width, gapWidthValue.toInt()),
            value = gapWidthValue,
            onValueChange = { newValue ->
                dataProvider.mutateData { data -> data.copy(gapWidthDegrees = newValue) }
            },
            valueRange = 0f..360f
        )

        // Gap angle
        SliderControl(
            title = stringResource(Res.string.gap_angle, gapAngleValue.toInt()),
            value = gapAngleValue,
            onValueChange = { newValue ->
                dataProvider.mutateData { data -> data.copy(gapAngleDegrees = newValue) }
            },
            valueRange = 0f..360f
        )

        // Stroke width
        SliderControl(
            title = stringResource(Res.string.stroke_width, strokeWidthValue.toInt()),
            value = strokeWidthValue,
            onValueChange = { newValue ->
                dataProvider.mutateData { data -> data.copy(strokeWidth = newValue) }
            },
            valueRange = 0f..100f
        )

        // Cap
        SliderControl(
            title = stringResource(Res.string.amount_cap, capValue.formatDecimals(2)),
            value = capValue,
            onValueChange = { newValue ->
                dataProvider.mutateData { data -> data.copy(cap = newValue) }
            },
            valueRange = 0f..20f
        )

        // Stroke cap
        StrokeCapsSpecsSelection(dataProvider = dataProvider)

        // Section: Data Manipulation
        Text(
            text = stringResource(Res.string.data_manipulation),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
        )

        // Data manipulation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(
                onClick = {
                    dataProvider.mutateData { data ->
                        data.copy(
                            sections = data.sections.map { section ->
                                section.copy(amount = section.amount + Random.nextFloat())
                            },
                        )
                    }
                },
            ) {
                Text(stringResource(Res.string.button_add))
            }

            Button(
                onClick = {
                    dataProvider.mutateData { data ->
                        data.copy(
                            sections = data.sections.map { section ->
                                section.copy(
                                    amount = (section.amount - Random.nextFloat()).coerceAtLeast(minimumValue = 0f),
                                )
                            },
                        )
                    }
                },
            ) {
                Text(stringResource(Res.string.button_remove))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(
                onClick = {
                    dataProvider.mutateData { data ->
                        data.copy(
                            sections = data.sections.map { section ->
                                section.copy(amount = Random.nextFloat() + Random.nextFloat())
                            },
                        )
                    }
                },
            ) {
                Text(stringResource(Res.string.button_random_values))
            }

            Button(
                onClick = {
                    dataProvider.mutateData { data ->
                        data.copy(
                            sections = data.sections.map { section ->
                                section.copy(color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()))
                            },
                        )
                    }
                },
            ) {
                Text(stringResource(Res.string.button_random_colors))
            }
        }

        Button(
            onClick = {
                dataProvider.mutateData { data ->
                    data.copy(
                        sections = data.sections.map { section ->
                            section.copy(amount = 0f)
                        },
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(Res.string.button_clear))
        }

        // Section: Animations
        Text(
            text = stringResource(Res.string.animations),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
        )

        // Animation duration
        val config = dataProvider.config.collectAsState()
        val animationDurationValue by remember {
            derivedStateOf {
                when (val layoutSpec = config.value.gapAngleAnimationSpec) {
                    is TweenSpec -> layoutSpec.durationMillis
                    else -> PlaygroundScreenData.INITIAL_ANIMATION_DURATION
                }
            }
        }

        SliderControl(
            title = stringResource(Res.string.animation_duration, animationDurationValue),
            value = animationDurationValue.toFloat(),
            onValueChange = { newValue ->
                val newDuration = newValue.toInt()
                dataProvider.mutateConfig { currentConfig ->
                    currentConfig.copyWithLayoutAnimationsSpec(
                        LayoutAnimationSpecs.LINEAR.getLayoutAnimationSpec(newDuration)
                    ).copyWithColorAnimationsSpec(
                        ColorAnimationSpecs.LINEAR.getColorAnimationSpec(newDuration)
                    )
                }
            },
            valueRange = 0f..3000f
        )

        // Layout animation type
        LayoutAnimationSpecsSelection(dataProvider = dataProvider, animDurationMs = animationDurationValue)

        // Color animation type
        ColorAnimationSpecsSelection(dataProvider = dataProvider, animDurationMs = animationDurationValue)
    }
}

@Composable
private fun StrokeCapsSpecsSelection(
    modifier: Modifier = Modifier,
    dataProvider: IPlaygroundScreenData,
) {
    val data = dataProvider.data.collectAsState()
    val radioOptions = remember { listOf(StrokeCap.Round, StrokeCap.Butt) }
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(data.value.strokeCap)
    }

    RadioButtonGroup(
        modifier = modifier,
        groupTitle = stringResource(Res.string.stroke_caps),
        radioOptions = radioOptions,
        selectedOption = selectedOption,
        onOptionSelected = { option ->
            onOptionSelected(option)
            dataProvider.mutateData { data ->
                data.copy(strokeCap = option)
            }
        },
        getDisplayTextResource = { option ->
            when (option) {
                StrokeCap.Round -> stringResource(Res.string.stroke_cap_round)
                StrokeCap.Butt -> stringResource(Res.string.stroke_cap_butt)
                else -> error("Unsupported stroke cap")
            }
        },
        getDisplayText = { it.toString() }
    )
}

@Composable
private fun LayoutAnimationSpecsSelection(
    modifier: Modifier = Modifier,
    dataProvider: IPlaygroundScreenData,
    animDurationMs: Int
) {
    val radioOptions = remember {
        listOf(
            LayoutAnimationSpecs.SNAP,
            LayoutAnimationSpecs.LINEAR,
            LayoutAnimationSpecs.SPRING,
            LayoutAnimationSpecs.FAST_OUT_SLOW_IN,
            LayoutAnimationSpecs.LINEAR_OUT_SLOW_IN,
            LayoutAnimationSpecs.FAST_OUT_LINEAR_IN,
        )
    }
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(LayoutAnimationSpecs.LINEAR)
    }

    RadioButtonGroup(
        modifier = modifier,
        groupTitle = stringResource(Res.string.layout_animation_type),
        radioOptions = radioOptions,
        selectedOption = selectedOption,
        onOptionSelected = { option ->
            onOptionSelected(option)
            dataProvider.mutateConfig { config ->
                config.copyWithLayoutAnimationsSpec(option.getLayoutAnimationSpec(animDurationMs))
            }
        },
        getDisplayText = { it.name }
    )
}

@Composable
private fun ColorAnimationSpecsSelection(
    modifier: Modifier = Modifier,
    dataProvider: IPlaygroundScreenData,
    animDurationMs: Int
) {
    val radioOptions = remember {
        listOf(
            ColorAnimationSpecs.SNAP,
            ColorAnimationSpecs.LINEAR,
            ColorAnimationSpecs.FAST_OUT_SLOW_IN,
            ColorAnimationSpecs.LINEAR_OUT_SLOW_IN,
            ColorAnimationSpecs.FAST_OUT_LINEAR_IN,
        )
    }
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(ColorAnimationSpecs.LINEAR)
    }

    RadioButtonGroup(
        modifier = modifier,
        groupTitle = stringResource(Res.string.color_animation_type),
        radioOptions = radioOptions,
        selectedOption = selectedOption,
        onOptionSelected = { option ->
            onOptionSelected(option)
            dataProvider.mutateConfig { config ->
                config.copyWithColorAnimationsSpec(option.getColorAnimationSpec(animDurationMs))
            }
        },
        getDisplayText = { it.name }
    )
}
