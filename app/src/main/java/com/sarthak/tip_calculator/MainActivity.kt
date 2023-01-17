package com.sarthak.tip_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sarthak.tip_calculator.components.MainBody
import com.sarthak.tip_calculator.ui.theme.Tip_CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tip_CalculatorTheme {
                TipCalculatorApp()
                
                
            }
        }
    }
}

@Composable
fun TipCalculatorApp() {
    Column(modifier = Modifier.fillMaxSize()) {
        MainBody()
    }

}