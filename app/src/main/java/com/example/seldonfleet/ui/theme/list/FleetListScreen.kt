package com.example.seldonfleet.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.domain.model.DeviceStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetListScreen(
    viewModel: FleetListViewModel,
    onDeviceClick: (Device) -> Unit
) {
    val devices by viewModel.devices.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    val countOk = devices.count { it.status == DeviceStatus.OK }
    val countAtencao = devices.count { it.status == DeviceStatus.ATENCAO }
    val countCritico = devices.count { it.status == DeviceStatus.CRITICO }
    val countIndeterminado = devices.count { it.status == DeviceStatus.INDETERMINADO }

    val filteredList = if (selectedFilter == null) {
        devices
    } else {
        devices.filter { it.status == selectedFilter }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Seldon Fleet - Monitoramento") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { viewModel.setFilter(null) },
                    label = { Text("Todos (${devices.size})") }
                )
                FilterChip(
                    selected = selectedFilter == DeviceStatus.OK,
                    onClick = { viewModel.setFilter(DeviceStatus.OK) },
                    label = { Text("OK ($countOk)") }
                )
                FilterChip(
                    selected = selectedFilter == DeviceStatus.ATENCAO,
                    onClick = { viewModel.setFilter(DeviceStatus.ATENCAO) },
                    label = { Text("Atenção ($countAtencao)") }
                )
                FilterChip(
                    selected = selectedFilter == DeviceStatus.CRITICO,
                    onClick = { viewModel.setFilter(DeviceStatus.CRITICO) },
                    label = { Text("Crítico ($countCritico)") }
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
                items(filteredList) { device ->
                    DeviceItemRow(
                        device = device,
                        onClick = { onDeviceClick(device) },
                        onFavoriteClick = { viewModel.toggleFavorite(device.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun DeviceItemRow(
    device: Device,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${device.id} - ${device.model}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Bateria: ${device.batteryLevelPct}% | Tier: ${device.tier}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                StatusChip(status = device.status)
            }

            IconButton(onClick = onFavoriteClick) {
                Text(text = if (device.isFavorite) "❤️" else "🤍")
            }
        }
    }
}

@Composable
fun StatusChip(status: DeviceStatus) {
    val (color, text) = when (status) {
        DeviceStatus.OK -> Color(0xFF4CAF50) to "OK"
        DeviceStatus.ATENCAO -> Color(0xFFFF9800) to "ATENÇÃO"
        DeviceStatus.CRITICO -> Color(0xFFF44336) to "CRÍTICO"
        DeviceStatus.INDETERMINADO -> Color(0xFF9E9E9E) to "SEM DADOS"
    }

    Surface(
        color = color.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}