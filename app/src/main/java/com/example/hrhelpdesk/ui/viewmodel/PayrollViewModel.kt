package com.example.hrhelpdesk.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrhelpdesk.data.Payroll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class PayrollViewModel : ViewModel() {
    private val _payrolls = MutableStateFlow<List<Payroll>>(emptyList())
    val payrolls: StateFlow<List<Payroll>> = _payrolls.asStateFlow()

    init {
        loadPayrolls()
    }

    private fun loadPayrolls() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH) + 1
            val currentYear = calendar.get(Calendar.YEAR)

            // Sample payroll data
            _payrolls.value = listOf(
                Payroll(
                    id = UUID.randomUUID().toString(),
                    month = currentMonth,
                    year = currentYear,
                    netSalary = 50000.0,
                    deductions = 2500.0,
                    attendanceDays = 22,
                    totalDays = 23
                ),
                Payroll(
                    id = UUID.randomUUID().toString(),
                    month = if (currentMonth == 1) 12 else currentMonth - 1,
                    year = if (currentMonth == 1) currentYear - 1 else currentYear,
                    netSalary = 48000.0,
                    deductions = 3000.0,
                    attendanceDays = 20,
                    totalDays = 22
                )
            )
        }
    }

    fun downloadPayslip(payrollId: String) {
        viewModelScope.launch {
            // Simulate PDF download
            // In a real app, this would generate and download a PDF
        }
    }
}

