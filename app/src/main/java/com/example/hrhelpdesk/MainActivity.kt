package com.example.hrhelpdesk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.hrhelpdesk.ui.navigation.MainNavigation
import com.example.hrhelpdesk.ui.theme.HRHelpdeskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HRHelpdeskTheme {
                MainNavigation(modifier = Modifier.fillMaxSize())
            }
        }
    }
}