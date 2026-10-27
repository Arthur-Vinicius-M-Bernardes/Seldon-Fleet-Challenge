package com.example.seldonfleet.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.ui.list.StatusChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(
    device: Device,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes: ${device.id}") },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("← Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = device.model, style = MaterialTheme.typography.headlineSmall)
            StatusChip(status = device.status)

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Justificativa da Classificação:", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = device.justification, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Atributos Técnicos:", style = MaterialTheme.typography.titleMedium)

            Text(text = "• SoC: ${device.soc} (Tier: ${device.tier})")
            Text(text = "• Memória RAM: ${device.ramMb} MB")
            Text(text = "• API Android: ${device.androidApi}")
            Text(text = "• Capacidade da Bateria: ${device.batteryCapacityMah} mAh")
            Text(text = "• Consumo Normalizado: ${"%.1f".format(device.energyConsumedNormalizedMah)} mAh")
            Text(text = "• Tempo de Tela Ativa: ${device.screenOnMinutes} min")
            Text(text = "• Jank (Quadros Perdidos): ${device.jankFramesPct}%")
            Text(text = "• Falhas Recentes (7d): ${device.crashCount7d}")
            Text(text = "• Último Contato: ${device.lastSeen}")
        }
    }
}