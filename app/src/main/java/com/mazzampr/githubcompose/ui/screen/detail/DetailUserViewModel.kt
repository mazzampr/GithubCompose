package com.mazzampr.githubcompose.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazzampr.githubcompose.data.UserRepository
import com.mazzampr.githubcompose.data.local.entity.UsersEntity
import com.mazzampr.githubcompose.data.remote.response.UserResponse
import com.mazzampr.githubcompose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailUserViewModel(private val repository: UserRepository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<UserResponse>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<UserResponse>> get() = _uiState

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getDetailUser(username).collect { user ->
                _uiState.value = UiState.Success(user)
            }
        }
    }

    fun getUserFavDetail(username: String) = repository.getUserFavDetail(username)

    fun insertFavorite(user: UsersEntity) {
        viewModelScope.launch {
            repository.insertFavorite(user)
        }
    }

    fun deleteFavorite(user: UsersEntity) {
        viewModelScope.launch {
            repository.deleteFavorite(user)
        }
    }
}