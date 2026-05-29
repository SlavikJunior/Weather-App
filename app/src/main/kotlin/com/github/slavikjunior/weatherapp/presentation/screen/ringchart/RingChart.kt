package com.github.slavikjunior.weatherapp.presentation.screen.ringchart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val START_ANGLE = 90f
private const val RING_GAP_DP = 6f
private const val HIGHLIGHT_FACTOR = 1.35f

private fun highlightColor(color: Color): Color {
    return Color(
        red = (color.red * HIGHLIGHT_FACTOR).coerceAtMost(1f),
        green = (color.green * HIGHLIGHT_FACTOR).coerceAtMost(1f),
        blue = (color.blue * HIGHLIGHT_FACTOR).coerceAtMost(1f),
        alpha = color.alpha
    )
}

@Composable
internal fun RingChart(
    data: RingChartData,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    Row(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .pointerInput(data) {
                    detectTapGestures { tapOffset ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val dist = (tapOffset - center).getDistance()
                        val totalRadius = minOf(size.width, size.height) / 2f
                        val gapPx = RING_GAP_DP * density
                        val n = data.values.size
                        val ringWidth = totalRadius / n

                        val ringIndex = ((totalRadius - dist) / ringWidth).toInt()
                        selectedIndex = if (ringIndex in 0 until n) {
                            if (ringIndex == selectedIndex) -1 else ringIndex
                        } else {
                            -1
                        }
                    }
                }
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val totalRadius = minOf(size.width, size.height) / 2f
            val n = data.values.size
            val gapPx = RING_GAP_DP.dp.toPx()
            val ringBand = totalRadius / n

            data.values.forEachIndexed { i, value ->
                val outerRadius = totalRadius - i * ringBand
                val strokeWidth = ringBand - gapPx
                val radius = outerRadius - strokeWidth / 2f - gapPx / 2f
                val topLeft = Offset(center.x - radius, center.y - radius)
                val arcSize = Size(radius * 2f, radius * 2f)

                val baseColor = data.colors[i]
                val sweepAngle = (value / 100f) * 360f
                val fillColor = if (i == selectedIndex) highlightColor(baseColor) else baseColor

                // Background track
                drawArc(
                    color = baseColor.copy(alpha = 0.2f),
                    startAngle = START_ANGLE,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth)
                )

                // Filled arc
                drawArc(
                    color = fillColor,
                    startAngle = START_ANGLE,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        // Legend
        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            data.values.forEachIndexed { i, value ->
                val isSelected = i == selectedIndex
                Box(modifier = Modifier.height(36.dp)) {
                    if (isSelected) {
                        Text(
                            text = "${value.toInt()}%",
                            color = highlightColor(data.colors[i]),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }
                }
                if (i < data.values.lastIndex) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
