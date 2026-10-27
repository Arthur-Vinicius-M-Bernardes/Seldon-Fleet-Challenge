package com.example.seldonfleet.domain.usecase

import com.example.seldonfleet.domain.model.DeviceStatus

data class ClassificationResult(
    val status: DeviceStatus,
    val justification: String
)

class ClassifyDevice {
    fun execute(
        batteryCapacityMah: Int,
        screenOnMinutes: Int,
        energyConsumed: Double,
        energyUnit: String,
        nominalVoltage: Double,
        jankFramesPct: Double,
        crashCount7d: Int
    ): ClassificationResult {
        // Defeito 2: Tratamento de registros incompletos / divisores nulos
        if (batteryCapacityMah <= 0 || screenOnMinutes <= 0) {
            return ClassificationResult(
                DeviceStatus.INDETERMINADO,
                "Indeterminado: Capacidade da bateria ($batteryCapacityMah mAh) ou tempo de tela ($screenOnMinutes min) inválido/ausente."
            )
        }

        if (energyUnit.lowercase() == "mwh" && nominalVoltage <= 0.0) {
            return ClassificationResult(
                DeviceStatus.INDETERMINADO,
                "Indeterminado: Unidade mWh informada sem tensão nominal válida ($nominalVoltage V)."
            )
        }

        // Defeito 1: Normalização de unidades (mWh para mAh via nominalVoltage)
        val energyMah = if (energyUnit.lowercase() == "mwh") {
            energyConsumed / nominalVoltage
        } else {
            energyConsumed
        }

        val screenHours = screenOnMinutes / 60.0
        val absoluteDrain = if (screenHours > 0) energyMah / screenHours else 0.0
        val relativeDrain = (absoluteDrain / batteryCapacityMah) * 100.0

        // Regras de Limiares e Precedência
        return when {
            relativeDrain >= 12.0 || crashCount7d >= 3 || jankFramesPct >= 15.0 -> {
                val trigger = mutableListOf<String>()
                if (relativeDrain >= 12.0) trigger.add("dreno de ${"%.1f".format(relativeDrain)}%/h (limiar ≥ 12%/h)")
                if (crashCount7d >= 3) trigger.add("$crashCount7d falhas recentes (limiar ≥ 3)")
                if (jankFramesPct >= 15.0) trigger.add("jank de ${"%.1f".format(jankFramesPct)}% (limiar ≥ 15%)")
                ClassificationResult(
                    DeviceStatus.CRITICO,
                    "Crítico: Disparado por ${trigger.joinToString(", ")}."
                )
            }
            relativeDrain >= 7.0 || crashCount7d >= 1 || jankFramesPct >= 8.0 -> {
                val trigger = mutableListOf<String>()
                if (relativeDrain >= 7.0) trigger.add("dreno de ${"%.1f".format(relativeDrain)}%/h (limiar ≥ 7%/h)")
                if (crashCount7d >= 1) trigger.add("$crashCount7d falha(s) (limiar ≥ 1)")
                if (jankFramesPct >= 8.0) trigger.add("jank de ${"%.1f".format(jankFramesPct)}% (limiar ≥ 8%)")
                ClassificationResult(
                    DeviceStatus.ATENCAO,
                    "Atenção: Sob observação devido a ${trigger.joinToString(", ")}."
                )
            }
            else -> {
                ClassificationResult(
                    DeviceStatus.OK,
                    "Saudável: Dreno relativo de ${"%.1f".format(relativeDrain)}%/h, sem anomalias críticas."
                )
            }
        }
    }
}