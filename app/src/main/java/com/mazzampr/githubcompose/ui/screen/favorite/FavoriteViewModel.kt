package com.mazzampr.githubcompose.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazzampr.githubcompose.data.UserRepository
import com.mazzampr.githubcompose.data.local.entity.UsersEntity
import com.mazzampr.githubcompose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: UserRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<UsersEntity>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<UsersEntity>>>
        get() = _uiState.asStateFlow()

    fun getAllFavUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getLikedUsers().collect { users ->
                _uiState.value = UiState.Success(users)
            }
        }
    }
}