package com.example.seldonfleet.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FleetRootDto(
    val schemaVersion: String,
    val exportedAt: String,
    val devices: List<DeviceDto>
)

@Serializable
data class DeviceDto(
    val id: String,
    val model: String,
    val soc: String,
    val tier: String,
    val ramMb: Int,
    val androidApi: Int,
    val batteryCapacityMah: Int = 0,
    val batteryLevelPct: Int,
    val screenOnMinutes: Int = 0,
    val energyConsumed: Double,
    val energyUnit: String,
    val nominalVoltage: Double = 0.0,
    val jankFramesPct: Double,
    val crashCount7d: Int,
    val lastSeen: String
)