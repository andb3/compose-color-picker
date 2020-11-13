package com.andb.apps.composecolorpickersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.andb.apps.composecolorpicker.ui.ExpandedColorPicker
import com.andb.apps.composecolorpicker.ui.MaterialPalette
import kotlin.random.Random

enum class Tabs {
    PICKER, PALETTE
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentTab = remember { mutableStateOf(Tabs.PICKER) }
            AppTheme {
                Scaffold (
                    topBar = {
                        Column(Modifier.drawShadow(4.dp)) {
                            TopAppBar(
                                title = { Text(text = "ComposeColorPicker") },
                                elevation = 0.dp
                            )
                            TabRow(selectedTabIndex = if (currentTab.value == Tabs.PICKER) 0 else 1) {
                                Tab(
                                    selected = currentTab.value == Tabs.PICKER,
                                    onClick = { currentTab.value = Tabs.PICKER },
                                    modifier = Modifier.padding(vertical = 16.dp)
                                ) {
                                    Text(text = "PICKER")
                                }
                                Tab(
                                    selected = currentTab.value == Tabs.PALETTE,
                                    onClick = { currentTab.value = Tabs.PALETTE },
                                    modifier = Modifier.padding(vertical = 16.dp)
                                ) {
                                    Text(text = "PALETTE")
                                }
                            }
                        }
                    },
                    bodyContent = {
                        val currentColor = remember { mutableStateOf(Color(1f, 0f, 0f)) }
                        Column {
                            when (currentTab.value) {
                                Tabs.PICKER -> {
                                    ExpandedColorPicker(selected = currentColor.value, modifier = Modifier.padding(32.dp)) {
                                        currentColor.value = it
                                    }
                                }
                                Tabs.PALETTE -> {
                                    MaterialPalette(selected = currentColor.value, modifier = Modifier.padding(32.dp)) {
                                        currentColor.value = it
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 32.dp)
                                    .background(currentColor.value, RoundedCornerShape(8.dp))
                                    .size(64.dp)
                                    .clickable {
                                        currentColor.value = Random.Default.run {
                                            Color(nextInt(255), nextInt(255), nextInt(255))
                                        }
                                    }
                            )
                        }
                    }
                )

            }
        }
    }
}