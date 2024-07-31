package com.bjelor.sandbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InterceptPlatformTextInput
import com.bjelor.sandbox.ui.theme.SandboxTheme
import kotlinx.coroutines.awaitCancellation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SandboxTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var basicFieldState by remember {
        mutableStateOf("")
    }
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DisableSoftKeyboard(true) {
            BasicTextField(
                modifier = Modifier.background(Color.Gray),
                value = basicFieldState,
                onValueChange = { basicFieldState = it }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DisableSoftKeyboard(disable: Boolean = true, content: @Composable () -> Unit) {
    InterceptPlatformTextInput(
        interceptor = { request, nextHandler ->
            // If this flag is changed while an input session is active, a new lambda instance
            // that captures the new value will be passed to InterceptPlatformTextInput, which
            // will automatically cancel the session upstream and restart it with this new
            // interceptor.
            if (!disable) {
                // Forward the request to the system.
                nextHandler.startInputMethod(request)
            } else {
                // This function has to return Nothing, and since we don't have any work to do
                // in this case, we just suspend until cancelled.
                awaitCancellation()
            }
        },
        content = content
    )
}
