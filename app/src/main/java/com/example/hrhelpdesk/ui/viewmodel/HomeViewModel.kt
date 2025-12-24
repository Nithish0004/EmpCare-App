package com.example.hrhelpdesk.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrhelpdesk.data.AttendanceStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

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

    private fun sendAttendanceDataToJson(status: AttendanceStatus, actionType: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
        
        val json = JSONObject().apply {
            put("action", actionType)
            put("timestamp", sdf.format(Date()))
            put("isWithinGeofence", status.isWithinGeofence)
            put("gpsVerified", status.gpsVerified)
            put("wifiVerified", status.wifiVerified)
            put("punchInTime", status.punchInTime?.let { sdf.format(it) } ?: JSONObject.NULL)
            put("punchOutTime", status.punchOutTime?.let { sdf.format(it) } ?: JSONObject.NULL)
        }

        // Log the JSON to simulate sending to server
        Log.d("AttendanceSync", "Sending JSON to server: ${json.toString(4)}")
        
        // In a real app with Retrofit, you would call your API here:
        // viewModelScope.launch {
        //     try {
        //         val response = apiService.syncAttendance(json.toString())
        //         if (response.isSuccessful) { /* handle success */ }
        //     } catch (e: Exception) { /* handle error */ }
        // }
    }

    fun punchIn() {
        viewModelScope.launch {
            val newStatus = AttendanceStatus(
                isActive = true,
                punchInTime = Date(),
                gpsVerified = true,
                wifiVerified = true,
                isWithinGeofence = true
            )
            _attendanceStatus.value = newStatus
            sendAttendanceDataToJson(newStatus, "PUNCH_IN")
        }
    }

    fun punchOut() {
        viewModelScope.launch {
            val currentInTime = _attendanceStatus.value.punchInTime
            val newStatus = AttendanceStatus(
                isActive = false,
                punchInTime = currentInTime,
                punchOutTime = Date(),
                gpsVerified = true,
                wifiVerified = true,
                isWithinGeofence = true
            )
            _attendanceStatus.value = newStatus
            sendAttendanceDataToJson(newStatus, "PUNCH_OUT")
        }
    }

    fun checkLocation() {
        viewModelScope.launch {
            val currentStatus = _attendanceStatus.value
            _attendanceStatus.value = currentStatus.copy(
                gpsVerified = true,
                wifiVerified = true,
                isWithinGeofence = true
            )
        }
    }
}
