package com.bogdan801.experiments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bogdan801.experiments.ui.theme.ExperimentsTheme
import com.bogdan801.util_library.intSettings
import com.bogdan801.util_library.stringSettings
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExperimentsTheme {
                val scope = rememberCoroutineScope()
                val savedString by stringSettings["text"].collectAsState(initial = null)
                val savedString2 by intSettings["number"].collectAsState(initial = null)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    var value by remember { mutableStateOf("") }
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = value,
                        onValueChange = { newValue ->
                            value = newValue
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row{
                        Button(
                            onClick = {
                                scope.launch {
                                    stringSettings.set("text", value)
                                }
                            }
                        ) {
                            Text(text = "Save to field 1")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    intSettings.set("number", value.toInt())
                                }
                            }
                        ) {
                            Text(text = "Save to field 2")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row{
                        Button(
                            onClick = {
                                scope.launch {
                                    stringSettings.set("text", null)
                                }
                            }
                        ) {
                            Text(text = "Clear field 1")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    intSettings.set("number", null)
                                }
                            }
                        ) {
                            Text(text = "Clear field 2")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "First field: $savedString")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Second field: $savedString2")
                }
            }
        }
    }
}