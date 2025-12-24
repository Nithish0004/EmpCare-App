package com.example.hrhelpdesk.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrhelpdesk.data.Employee
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _employee = MutableStateFlow(
        Employee(
            id = "1",
            name = "John Doe",
            email = "john.doe@company.com",
            department = "Engineering",
            employeeId = "EMP001"
        )
    )
    val employee: StateFlow<Employee> = _employee.asStateFlow()
    
    fun updateEmployee(
        name: String,
        email: String,
        department: String
    ) {
        viewModelScope.launch {
            _employee.value = _employee.value.copy(
                name = name.trim(),
                email = email.trim(),
                department = department.trim()
            )
            // In a real app, you would save to backend/database here
        }
    }
}

