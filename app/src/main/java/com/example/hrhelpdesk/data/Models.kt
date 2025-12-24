package com.example.hrhelpdesk.data

import java.util.Date

data class Employee(
    val id: String,
    val name: String,
    val email: String,
    val department: String,
    val employeeId: String
)

data class AttendanceStatus(
    val isActive: Boolean,
    val punchInTime: Date? = null,
    val punchOutTime: Date? = null,
    val gpsVerified: Boolean = false,
    val wifiVerified: Boolean = false,
    val isWithinGeofence: Boolean = true
)

data class LeaveRequest(
    val id: String,
    val leaveType: LeaveType,
    val startDate: Date,
    val endDate: Date,
    val reason: String,
    val status: LeaveStatus,
    val submittedDate: Date
)

enum class LeaveType {
    CASUAL,
    PAID,
    SICK,
    EMERGENCY
}

enum class LeaveStatus {
    PENDING,
    APPROVED,
    REJECTED
}

data class Payroll(
    val id: String,
    val month: Int,
    val year: Int,
    val netSalary: Double,
    val deductions: Double,
    val attendanceDays: Int,
    val totalDays: Int
)

data class ChatMessage(
    val id: String,
    val message: String,
    val isUser: Boolean,
    val timestamp: Date = Date()
)

