package com.example.seldonfleet.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.seldonfleet.data.dto.FleetRootDto
import com.example.seldonfleet.data.mapper.DeviceMapper
import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "seldon_preferences")

class FleetRepositoryImpl(
    private val context: Context,
    private val mapper: DeviceMapper
) : DeviceRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getDevices(): List<Device> {
        val jsonString = context.assets.open("frota.json").bufferedReader().use { it.readText() }
        val rootDto = json.decodeFromString<FleetRootDto>(jsonString)

        val prefs = context.dataStore.data.first()

        return rootDto.devices.map { dto ->
            val favKey = booleanPreferencesKey("fav_${dto.id}")
            val isFav = prefs[favKey] ?: false
            mapper.toDomain(dto, isFav)
        }
    }

    override suspend fun toggleFavorite(deviceId: String) {
        val favKey = booleanPreferencesKey("fav_$deviceId")
        context.dataStore.edit { prefs ->
            val current = prefs[favKey] ?: false
            prefs[favKey] = !current
        }
    }
}