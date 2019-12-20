package app.futured.donut

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.core.graphics.flatten
import app.futured.donut.extensions.hasDuplicatesBy
import app.futured.donut.extensions.sumByFloat

class DonutProgressView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0,
    private val defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val TAG = "DonutProgressView"
        private const val DEBUG_COLLISIONS = false

        private const val DEFAULT_MASTER_PROGRESS = 1f
        private const val DEFAULT_STROKE_WIDTH_DP = 12f
        private const val DEFAULT_GAP_WIDTH = 45f
        private const val DEFAULT_GAP_ANGLE = 90f
        private const val DEFAULT_CAP = 1f
        private val DEFAULT_BG_COLOR_RES = R.color.grey

        private const val DEFAULT_ANIM_ENABLED = true
        private val DEFAULT_INTERPOLATOR = DecelerateInterpolator(1.5f)
        private const val DEFAULT_ANIM_DURATION_MS = 1000
    }

    interface DatasetClickListener {
        fun onClick(datasetName: String)
    }

    private var w = 0
    private var h = 0
    private var xPadd = 0f
    private var yPadd = 0f

    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f

    /**
     * Percentage of progress shown for all lines.
     *
     * Eg. when one line has 50% of total graph length,
     * setting this to 0.5f will result in that line being animated to 25% of total graph length.
     */
    var masterProgress: Float = DEFAULT_MASTER_PROGRESS
        set(value) {
            if (value !in 0f..1f) {
                return
            }

            field = value

            bgLine.mMasterProgress = value
            lines.forEach { it.mMasterProgress = value }
            invalidate()
        }

    /**
     * Stroke width of all lines in pixels.
     */
    var strokeWidth = dpToPx(DEFAULT_STROKE_WIDTH_DP)
        set(value) {
            field = value

            bgLine.mLineStrokeWidth = value
            lines.forEach { it.mLineStrokeWidth = value }
            updateLinesRadius()
            invalidate()
        }

    /**
     * Maximum value of sum of all entries in view, after which
     * all lines start to resize proportionally to amounts in their entry categories.
     */
    var cap: Float = DEFAULT_CAP
        set(value) {
            field = value
            resolveState()
        }

    /**
     * Color of background line.
     */
    @ColorInt
    var bgLineColor: Int = ContextCompat.getColor(context, DEFAULT_BG_COLOR_RES)
        set(value) {
            field = value
            bgLine.mLineColor = value
            invalidate()
        }

    /**
     * Size of gap opening in degrees.
     */
    var gapWidthDegrees: Float = DEFAULT_GAP_WIDTH
        set(value) {
            field = value

            bgLine.mGapWidthDegrees = value
            lines.forEach { it.mGapWidthDegrees = value }
            invalidate()
        }

    /**
     * Angle in degrees, at which the gap will be displayed.
     */
    var gapAngleDegrees: Float = DEFAULT_GAP_ANGLE
        set(value) {
            field = value

            bgLine.mGapAngleDegrees = value
            lines.forEach { it.mGapAngleDegrees = value }
            invalidate()
        }

    /**
     * If true, view will animate changes when new data is submitted.
     * If false, state change will happen instantly.
     */
    var animateChanges: Boolean = DEFAULT_ANIM_ENABLED

    /**
     * Interpolator used for state change animations.
     */
    var animationInterpolator: Interpolator = DEFAULT_INTERPOLATOR

    /**
     * Duration of state change animations.
     */
    var animationDurationMs: Long = DEFAULT_ANIM_DURATION_MS.toLong()

    private val data = mutableListOf<DonutDataset>()
    private val lines = mutableListOf<DonutProgressLine>()
    private var animatorSet: AnimatorSet? = null

    private var blackPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 2f
        color = Color.BLACK
    }

    private var redPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 2f
        color = Color.RED
    }

    private val touchTarget = dpToPx(24f)

    private var tapRect: Rect? = null
    private var collisionRects: MutableList<Pair<DonutProgressLine, Rect>> = mutableListOf()

    private var datasetClickListener: DatasetClickListener? = null

    private val bgLine = DonutProgressLine(
        name = "_bg",
        radius = radius,
        lineColor = bgLineColor,
        lineStrokeWidth = strokeWidth,
        masterProgress = masterProgress,
        length = 1f,
        gapWidthDegrees = gapWidthDegrees,
        gapAngleDegrees = gapAngleDegrees
    )

    init {
        obtainAttributes()

        val detector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent) = true

            override fun onSingleTapConfirmed(e: MotionEvent) = detectTap(e.x, e.y)
        })

        setOnTouchListener { _, event ->
            detector.onTouchEvent(event)
        }
    }

    @SuppressLint("Recycle")
    private fun obtainAttributes() {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.DonutProgressView,
            defStyleAttr,
            defStyleRes
        ).use {
            strokeWidth = it.getDimensionPixelSize(
                R.styleable.DonutProgressView_donut_strokeWidth,
                dpToPx(DEFAULT_STROKE_WIDTH_DP).toInt()
            ).toFloat()

            bgLineColor =
                it.getColor(
                    R.styleable.DonutProgressView_donut_bgLineColor,
                    ContextCompat.getColor(
                        context,
                        DEFAULT_BG_COLOR_RES
                    )
                )

            gapWidthDegrees =
                it.getFloat(R.styleable.DonutProgressView_donut_gapWidth, DEFAULT_GAP_WIDTH)
            gapAngleDegrees =
                it.getFloat(R.styleable.DonutProgressView_donut_gapAngle, DEFAULT_GAP_ANGLE)

            animateChanges = it.getBoolean(
                R.styleable.DonutProgressView_donut_animateChanges,
                DEFAULT_ANIM_ENABLED
            )

            animationDurationMs = it.getInt(
                R.styleable.DonutProgressView_donut_animationDuration,
                DEFAULT_ANIM_DURATION_MS
            ).toLong()

            animationInterpolator =
                it.getResourceId(R.styleable.DonutProgressView_donut_animationInterpolator, 0)
                    .let { id ->
                        if (id != 0) {
                            AnimationUtils.loadInterpolator(context, id)
                        } else {
                            DEFAULT_INTERPOLATOR
                        }
                    }

            cap = it.getFloat(R.styleable.DonutProgressView_donut_cap, DEFAULT_CAP)
        }
    }

    /**
     * Returns current data.
     */
    fun getData() = data.toList()

    /**
     * Submits new [datasets] to the view.
     *
     * New progress line will be created for each non-existent dataset and view will be animated to new state.
     * Additionally, existing lines with no data set will be removed when animation completes.
     */
    fun submitData(datasets: List<DonutDataset>) {
        assertDataConsistency(datasets)

        datasets
            .filter { it.amount > 0f }
            .forEach { dataset ->
                val newLineColor = dataset.color
                if (hasEntriesForDataset(dataset.name).not()) {
                    lines.add(
                        index = 0,
                        element = DonutProgressLine(
                            name = dataset.name,
                            radius = radius,
                            lineColor = newLineColor,
                            lineStrokeWidth = strokeWidth,
                            masterProgress = masterProgress,
                            length = 0f,
                            gapWidthDegrees = gapWidthDegrees,
                            gapAngleDegrees = gapAngleDegrees
                        )
                    )
                } else {
                    lines
                        .filter { it.name == dataset.name }
                        .forEach { it.mLineColor = newLineColor }
                }
            }

        this.data.apply {
            val copy = ArrayList(datasets)
            clear()
            addAll(copy)
        }

        resolveState()
    }

    /**
     * Adds [amount] to existing dataset specified by [datasetName]. If dataset does not exist and [color] is specified,
     * creates new dataset internally.
     */
    fun addAmount(datasetName: String, amount: Float, color: Int? = null) {
        for (i in 0 until data.size) {
            if (data[i].name == datasetName) {
                data[i] = data[i].copy(amount = data[i].amount + amount)
                submitData(data)
                return
            }
        }

        color?.let {
            submitData(
                data + DonutDataset(
                    name = datasetName,
                    color = it,
                    amount = amount
                )
            )
        }
            ?: warn {
                "Adding amount to non-existent dataset: $datasetName. " +
                        "Please specify color, if you want to have dataset created automatically."
            }
    }

    /**
     * Sets [amount] for existing dataset specified by [datasetName].
     * Removes dataset if amount is <= 0.
     * Does nothing if dataset does not exist.
     */
    fun setAmount(datasetName: String, amount: Float) {
        for (i in 0 until data.size) {
            if (data[i].name == datasetName) {
                if (amount > 0) {
                    data[i] = data[i].copy(amount = amount)
                } else {
                    data.removeAt(i)
                }
                submitData(data)
                return
            }
        }

        warn { "Setting amount for non-existent dataset: $datasetName" }
    }

    /**
     * Removes [amount] from existing dataset specified by [datasetName].
     * If amount gets below zero, removes the dataset from view.
     */
    fun removeAmount(datasetName: String, amount: Float) {
        for (i in 0 until data.size) {
            if (data[i].name == datasetName) {
                val resultAmount = data[i].amount - amount
                if (resultAmount > 0) {
                    data[i] = data[i].copy(amount = resultAmount)
                } else {
                    data.removeAt(i)
                }
                submitData(data)
                return
            }
        }

        warn { "Removing amount from non-existend dataset: $datasetName" }
    }

    /**
     * Clear data, removing all lines.
     */
    fun clear() = submitData(listOf())

    /**
     * Sets dataset line click listener. This is experimental feature.
     */
    fun setDatasetClickListener(listener: DatasetClickListener) {
        this.datasetClickListener = listener
    }

    private fun assertDataConsistency(data: List<DonutDataset>) {
        if (data.hasDuplicatesBy { it.name }) {
            throw IllegalStateException("Multiple datasets with same name found")
        }
    }

    private fun resolveState() {
        animatorSet?.cancel()
        animatorSet = AnimatorSet()

        val drawPercentages = calculateLineDrawPercentages()

        drawPercentages.forEachIndexed { index, newPercentage ->
            val line = lines[index]
            val animator = animateLine(line, newPercentage) {
                if (!hasEntriesForDataset(line.name)) {
                    removeLine(line)
                }
            }

            animatorSet?.play(animator)
        }

        animatorSet?.start()
    }

    private fun detectTap(tapX: Float, tapY: Float): Boolean {
        tapRect = Rect(
            (tapX - touchTarget / 2).toInt(),
            (tapY - touchTarget / 2).toInt(),
            (tapX + touchTarget / 2).toInt(),
            (tapY + touchTarget / 2).toInt()
        )

        collisionRects.clear()

        val drawPercentages = calculateLineDrawPercentages().reversed()

        lines
            .reversed()
            .forEachIndexed { index, line ->
                val lineSegments = line.path.flatten()
                val visibleIndexStart = if (index == 0) {
                    0
                } else {
                    (lineSegments.count() * drawPercentages[index - 1] * line.mMasterProgress).toInt()
                }
                val visibleIndexEnd =
                    (lineSegments.count() * drawPercentages[index] * line.mMasterProgress).toInt()

                // Calculate collision rectangles that intersect with tap rectangle
                lineSegments
                    .toList()
                    .subList(visibleIndexStart, visibleIndexEnd)
                    .map { lineSegment ->
                        Rect(
                            lineSegment.start.x.toInt(),
                            lineSegment.start.y.toInt(),
                            lineSegment.end.x.toInt(),
                            lineSegment.end.y.toInt()
                        ).apply {
                            sort()
                            offset(centerX.toInt(), centerY.toInt())
                        }
                    }
                    .filter { rect ->
                        tapRect?.intersects(rect.left, rect.top, rect.right, rect.bottom) ?: false
                    }
                    .map { line to it }
                    .also {
                        collisionRects.addAll(it)
                    }
            }

        lines
            .map { line ->
                line to collisionRects.count { it.first == line }
            }
            .sortedByDescending { it.second }
            .filter { it.second > 0 }
            .map { it.first }
            .firstOrNull()?.let { line ->
                datasetClickListener?.onClick(line.name)
            }

        if (DEBUG_COLLISIONS) {
            invalidate()
        }

        return collisionRects.isNotEmpty()
    }

    private fun calculateLineDrawPercentages(): List<Float> {
        val datasetAmounts = lines.map { getAmountForDataset(it.name) }
        val totalAmount = datasetAmounts.sumByFloat { it }

        return datasetAmounts.mapIndexed { index, _ ->
            if (totalAmount > cap) {
                getDrawAmountForLine(datasetAmounts, index) / totalAmount
            } else {
                getDrawAmountForLine(datasetAmounts, index) / cap
            }
        }
    }

    private fun getAmountForDataset(dataset: String): Float {
        return data
            .filter { it.name == dataset }
            .sumByFloat { it.amount }
    }

    private fun getDrawAmountForLine(amounts: List<Float>, index: Int): Float {
        if (index >= amounts.size) {
            return 0f
        }

        val thisLine = amounts[index]
        val previousLine = getDrawAmountForLine(amounts, index + 1) // Length of line above this one

        return thisLine + previousLine
    }

    private fun hasEntriesForDataset(dataset: String) =
        data.indexOfFirst { it.name == dataset } > -1

    private fun animateLine(
        line: DonutProgressLine,
        to: Float,
        animationEnded: (() -> Unit)? = null
    ): ValueAnimator {
        return ValueAnimator.ofFloat(line.mLength, to).apply {
            duration = if (animateChanges) animationDurationMs else 0L
            interpolator = animationInterpolator
            addUpdateListener {
                (it.animatedValue as? Float)?.let { animValue ->
                    line.mLength = animValue
                }
                invalidate()
            }

            doOnEnd {
                animationEnded?.invoke()
            }
        }
    }

    private fun removeLine(line: DonutProgressLine) {
        lines.remove(line)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.w = w
        this.h = h

        this.xPadd = (paddingLeft + paddingRight).toFloat()
        this.yPadd = (paddingTop + paddingBottom).toFloat()

        this.centerX = w / 2f
        this.centerY = h / 2f

        updateLinesRadius()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val originalWidth = MeasureSpec.getSize(widthMeasureSpec)

        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY)
        )
    }

    private fun updateLinesRadius() {
        val ww = w.toFloat() - xPadd
        val hh = h.toFloat() - yPadd
        this.radius = Math.min(ww, hh) / 2f - strokeWidth / 2f

        bgLine.mRadius = radius
        lines.forEach { it.mRadius = radius }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.translate(centerX, centerY)

        bgLine.draw(canvas)
        lines.forEach { it.draw(canvas) }

        if (DEBUG_COLLISIONS) {
            drawDebugTapCollisions(canvas)
        }
    }

    private fun drawDebugTapCollisions(canvas: Canvas) {
        canvas.translate(-centerX, -centerY)
        collisionRects.forEach {
            canvas.drawRect(it.second, redPaint)
        }
        tapRect?.let { canvas.drawRect(it, blackPaint) }
    }

    private fun d(message: () -> String) {
        if (BuildConfig.DEBUG) {
            Log.d("DonutProgressView", message())
        }
    }

    private fun dpToPx(dp: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )

    private fun warn(text: () -> String) {
        Log.w(TAG, text())
    }
}
