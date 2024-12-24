package com.nsicyber.mojimapper.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(modifier: Modifier, icon: Int, onClick: () -> Unit) {


    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .background(Color.White)

            .size(48.dp)
            .padding(6.dp)
    ) {


        Image(
            modifier = Modifier.fillMaxSize().clip(CircleShape),
            painter = painterResource(icon),
            contentDescription = "",
            colorFilter = ColorFilter.tint(Color.Black)
        )

    }
}

