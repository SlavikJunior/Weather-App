package com.github.slavikjunior.weatherapp.presentation.screen.ringchart

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
internal data class RingChartData(
    val values: List<Float>,
    val colors: List<Color>
) {
    init {
        require(values.size in 2..7) { "Ring count must be between 2 and 7" }
        require(values.size == colors.size) { "Values and colors must have the same size" }
        require(colors.distinct().size == colors.size) { "Colors must be unique" }
        require(values.all { it in 1f..100f }) { "Each value must be between 1 and 100" }
    }
}
