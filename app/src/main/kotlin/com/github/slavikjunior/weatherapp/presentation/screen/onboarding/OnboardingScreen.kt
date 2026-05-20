package com.github.slavikjunior.weatherapp.presentation.screen.onboarding

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.SlavikJunior.weatherapp.R
import com.github.slavikjunior.weatherapp.presentation.ui.theme.CardBackground
import com.github.slavikjunior.weatherapp.presentation.ui.theme.GradientBottom
import com.github.slavikjunior.weatherapp.presentation.ui.theme.TextOnGradient
import com.github.slavikjunior.weatherapp.presentation.ui.theme.TextSecondaryOnGradient
import com.google.firebase.analytics.FirebaseAnalytics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingBottomSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    LaunchedEffect(Unit) {
        FirebaseAnalytics.getInstance(context).logEvent("onboarding_shown", Bundle.EMPTY)
    }

    BackHandler(enabled = true) {}

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        containerColor = GradientBottom
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.onboarding_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextOnGradient
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.onboarding_welcome),
                fontSize = 16.sp,
                color = TextSecondaryOnGradient,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            stringArrayResource(R.array.onboarding_features).forEach { feature ->
                Text(
                    text = "• $feature",
                    fontSize = 15.sp,
                    color = TextOnGradient,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    FirebaseAnalytics.getInstance(context).logEvent("onboarding_dismissed", Bundle.EMPTY)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CardBackground)
            ) {
                Text(text = stringResource(R.string.onboarding_dismiss), fontSize = 16.sp, color = TextOnGradient)
            }
        }
    }
}
