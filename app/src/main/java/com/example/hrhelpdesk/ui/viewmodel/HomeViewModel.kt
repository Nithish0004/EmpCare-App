package com.example.hrhelpdesk.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrhelpdesk.data.AttendanceStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel : ViewModel() {
    private val _attendanceStatus = MutableStateFlow(
        AttendanceStatus(
            isActive = false,
            gpsVerified = false,
            wifiVerified = false,
            isWithinGeofence = true
        )
    )
    val attendanceStatus: StateFlow<AttendanceStatus> = _attendanceStatus.asStateFlow()

    init {
        checkLocation()
    }

    fun punchIn() {
        viewModelScope.launch {
            // Simulate location verification
            _attendanceStatus.value = AttendanceStatus(
                isActive = true,
                punchInTime = Date(),
                gpsVerified = true,
                wifiVerified = true,
                isWithinGeofence = true
            )
        }
    }

    fun punchOut() {
        viewModelScope.launch {
            _attendanceStatus.value = AttendanceStatus(
                isActive = false,
                punchInTime = _attendanceStatus.value.punchInTime,
                punchOutTime = Date(),
                gpsVerified = true,
                wifiVerified = true,
                isWithinGeofence = true
            )
        }
    }

    fun checkLocation() {
        viewModelScope.launch {
            // Simulate location check
            val currentStatus = _attendanceStatus.value
            _attendanceStatus.value = currentStatus.copy(
                gpsVerified = true,
                wifiVerified = true,
                isWithinGeofence = true
            )
        }
    }
}

