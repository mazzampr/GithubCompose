package com.mazzampr.githubcompose.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazzampr.githubcompose.data.UserRepository
import com.mazzampr.githubcompose.data.remote.response.UserResponse
import com.mazzampr.githubcompose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<UserResponse>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<UserResponse>>>
        get() = _uiState

    var query by mutableStateOf("")

    fun getAllUsers() {
        viewModelScope.launch {
            repository.getAllUsers()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { users ->
                    _uiState.value = UiState.Success(users)
                }
        }
    }

    fun findSearchUsers(username: String) {
        viewModelScope.launch {
            repository.getSearchUsers(username)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { users ->
                    _uiState.value = UiState.Success(users)
                }
        }
    }

}