package com.example.primebus.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SearchScreen() {
    Box(modifier = Modifier.fillMaxSize()){
        Text(
            text = "Search Screen", modifier = Modifier.align(Alignment.Center)
        )
    }
}