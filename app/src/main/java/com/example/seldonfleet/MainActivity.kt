package com.example.seldonfleet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import com.example.seldonfleet.ui.detail.DeviceDetailScreen
import com.example.seldonfleet.ui.list.FleetListScreen
import com.example.seldonfleet.ui.list.FleetListViewModel
import com.example.seldonfleet.domain.model.Device

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: FleetListViewModel = viewModel()
            var selectedDevice by remember { mutableStateOf<Device?>(null) }

            if (selectedDevice == null) {
                FleetListScreen(
                    viewModel = viewModel,
                    onDeviceClick = { device -> selectedDevice = device }
                )
            } else {
                DeviceDetailScreen(
                    device = selectedDevice!!,
                    onBackClick = { selectedDevice = null }
                )
            }
        }
    }
}