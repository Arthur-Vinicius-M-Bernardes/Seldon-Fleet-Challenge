package com.example.seldonfleet.data.mapper

import com.example.seldonfleet.data.dto.DeviceDto
import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.domain.usecase.ClassifyDevice

class DeviceMapper(private val classifyDevice: ClassifyDevice) {
    fun toDomain(dto: DeviceDto, isFavorite: Boolean): Device {
        val classification = classifyDevice.execute(
            batteryCapacityMah = dto.batteryCapacityMah,
            screenOnMinutes = dto.screenOnMinutes,
            energyConsumed = dto.energyConsumed,
            energyUnit = dto.energyUnit,
            nominalVoltage = dto.nominalVoltage,
            jankFramesPct = dto.jankFramesPct,
            crashCount7d = dto.crashCount7d
        )

        val normalizedEnergy = if (dto.energyUnit.lowercase() == "mwh" && dto.nominalVoltage > 0) {
            dto.energyConsumed / dto.nominalVoltage
        } else {
            dto.energyConsumed
        }

        return Device(
            id = dto.id,
            model = dto.model,
            soc = dto.soc,
            tier = dto.tier,
            ramMb = dto.ramMb,
            androidApi = dto.androidApi,
            batteryCapacityMah = dto.batteryCapacityMah,
            batteryLevelPct = dto.batteryLevelPct,
            screenOnMinutes = dto.screenOnMinutes,
            energyConsumedNormalizedMah = normalizedEnergy,
            nominalVoltage = dto.nominalVoltage,
            jankFramesPct = dto.jankFramesPct,
            crashCount7d = dto.crashCount7d,
            lastSeen = dto.lastSeen,
            status = classification.status,
            justification = classification.justification,
            isFavorite = isFavorite
        )
    }
}