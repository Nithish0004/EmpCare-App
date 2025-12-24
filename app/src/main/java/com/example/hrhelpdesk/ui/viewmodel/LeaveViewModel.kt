package com.example.hrhelpdesk.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrhelpdesk.data.LeaveRequest
import com.example.hrhelpdesk.data.LeaveStatus
import com.example.hrhelpdesk.data.LeaveType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit

class LeaveViewModel : ViewModel() {
    private val _leaveRequests = MutableStateFlow<List<LeaveRequest>>(emptyList())
    val leaveRequests: StateFlow<List<LeaveRequest>> = _leaveRequests.asStateFlow()

    val casualLeavesUsed: StateFlow<Int> = _leaveRequests.map { requests ->
        calculateUsedLeaves(requests, LeaveType.CASUAL)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val paidLeavesUsed: StateFlow<Int> = _leaveRequests.map { requests ->
        calculateUsedLeaves(requests, LeaveType.PAID)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val casualTotal = 6
    val paidTotal = 12

    init {
        // Sample data
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        
        _leaveRequests.value = listOf(
            LeaveRequest(
                id = UUID.randomUUID().toString(),
                leaveType = LeaveType.CASUAL,
                startDate = createDate(currentYear, Calendar.JANUARY, 10),
                endDate = createDate(currentYear, Calendar.JANUARY, 12),
                reason = "Family event",
                status = LeaveStatus.APPROVED,
                submittedDate = Date()
            ),
            LeaveRequest(
                id = UUID.randomUUID().toString(),
                leaveType = LeaveType.PAID,
                startDate = createDate(currentYear, Calendar.FEBRUARY, 15),
                endDate = createDate(currentYear, Calendar.FEBRUARY, 19),
                reason = "Vacation",
                status = LeaveStatus.APPROVED,
                submittedDate = Date()
            ),
            LeaveRequest(
                id = UUID.randomUUID().toString(),
                leaveType = LeaveType.SICK,
                startDate = Date(System.currentTimeMillis() + 86400000 * 2),
                endDate = Date(System.currentTimeMillis() + 86400000 * 2),
                reason = "Medical appointment",
                status = LeaveStatus.PENDING,
                submittedDate = Date(System.currentTimeMillis() - 86400000)
            )
        )
    }

    private fun createDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }

    private fun calculateUsedLeaves(requests: List<LeaveRequest>, type: LeaveType): Int {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return requests.filter { 
            it.leaveType == type && it.status == LeaveStatus.APPROVED 
        }.filter {
            val cal = Calendar.getInstance()
            cal.time = it.startDate
            cal.get(Calendar.YEAR) == currentYear
        }.sumOf { 
            val diff = it.endDate.time - it.startDate.time
            val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt() + 1
            days
        }
    }

    fun submitLeaveRequest(
        leaveType: LeaveType,
        startDate: Date,
        endDate: Date,
        reason: String
    ) {
        viewModelScope.launch {
            val newRequest = LeaveRequest(
                id = UUID.randomUUID().toString(),
                leaveType = leaveType,
                startDate = startDate,
                endDate = endDate,
                reason = reason,
                status = LeaveStatus.PENDING,
                submittedDate = Date()
            )
            _leaveRequests.value = _leaveRequests.value + newRequest
        }
    }
}
