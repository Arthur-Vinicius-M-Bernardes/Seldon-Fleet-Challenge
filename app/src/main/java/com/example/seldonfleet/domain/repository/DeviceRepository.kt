package com.example.seldonfleet.domain.repository

import com.example.seldonfleet.domain.model.Device

interface DeviceRepository {
    suspend fun getDevices(): List<Device>
    suspend fun toggleFavorite(deviceId: String)
}