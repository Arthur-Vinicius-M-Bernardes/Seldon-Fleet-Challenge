package com.example.seldonfleet.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.seldonfleet.data.mapper.DeviceMapper
import com.example.seldonfleet.data.repository.FleetRepositoryImpl
import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.domain.model.DeviceStatus
import com.example.seldonfleet.domain.usecase.ClassifyDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FleetListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FleetRepositoryImpl(
        context = application,
        mapper = DeviceMapper(ClassifyDevice())
    )

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val _selectedFilter = MutableStateFlow<DeviceStatus?>(null)
    val selectedFilter: StateFlow<DeviceStatus?> = _selectedFilter.asStateFlow()

    init {
        loadDevices()
    }

    fun loadDevices() {
        viewModelScope.launch {
            try {
                _devices.value = repository.getDevices()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setFilter(status: DeviceStatus?) {
        _selectedFilter.value = status
    }

    fun toggleFavorite(deviceId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(deviceId)
            loadDevices()
        }
    }
}