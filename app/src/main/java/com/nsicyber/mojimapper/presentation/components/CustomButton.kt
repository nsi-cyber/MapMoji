package com.nsicyber.mojimapper.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun CustomButton(modifier: Modifier, icon: Int, onClick: () -> Unit) {
    Box(modifier = modifier     .clip(RoundedCornerShape(12.dp))   .clickable { onClick() }
        .border(
            BorderStroke(2.dp, Color.White.copy(alpha = 0.8f)),
            RoundedCornerShape(12.dp)
        )


        .background(Color.Black.copy(alpha = 0.5f))

        .size(64.dp)
        .padding(12.dp)) {
        Image(
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(icon),
            contentDescription = ""
        )
    }
}