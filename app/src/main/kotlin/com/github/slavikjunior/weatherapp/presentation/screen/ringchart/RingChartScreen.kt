package com.github.slavikjunior.weatherapp.presentation.screen.ringchart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.SlavikJunior.weatherapp.R
import com.github.slavikjunior.weatherapp.presentation.ui.theme.GradientBottom
import com.github.slavikjunior.weatherapp.presentation.ui.theme.GradientTop
import com.github.slavikjunior.weatherapp.presentation.ui.theme.TextOnGradient

@Composable
fun RingChartScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onBack: () -> Unit = {}
) {
    val gradient = remember { Brush.verticalGradient(listOf(GradientTop, GradientBottom)) }

    val chartData = remember {
        RingChartData(
            values = listOf(80f, 55f, 35f, 70f),
            colors = listOf(
                Color(0xFF4FC3F7),
                Color(0xFFAED581),
                Color(0xFFFFB74D),
                Color(0xFFBA68C8)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(text = stringResource(R.string.action_back), fontSize = 16.sp, color = TextOnGradient)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.ring_chart_label),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = TextOnGradient
            )

            Spacer(modifier = Modifier.height(32.dp))

            RingChart(
                data = chartData,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.ring_chart_hint),
                fontSize = 14.sp,
                color = TextOnGradient.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
