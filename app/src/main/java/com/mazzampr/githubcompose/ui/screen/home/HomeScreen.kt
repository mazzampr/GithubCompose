package com.mazzampr.githubcompose.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mazzampr.githubcompose.R
import com.mazzampr.githubcompose.data.remote.response.UserResponse
import com.mazzampr.githubcompose.di.Injection
import com.mazzampr.githubcompose.ui.ViewModelFactory
import com.mazzampr.githubcompose.ui.common.UiState
import com.mazzampr.githubcompose.ui.components.SearchBar
import com.mazzampr.githubcompose.ui.components.UserListItem

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    navigateToDetail: (String) -> Unit,
    navigateToFavorite: () -> Unit
)  {
    val keyboard = LocalSoftwareKeyboardController.current
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let {uiState->
        when(uiState) {
            is UiState.Loading -> {
                if (viewModel.query.isEmpty()) {
                    viewModel.getAllUsers()
                } else {
                    viewModel.findSearchUsers(viewModel.query)
                }
                Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            is UiState.Success -> {
                HomeContent(
                    favUser = uiState.data,
                    query = viewModel.query,
                    onQueryChange = { viewModel.query = it },
                    onSearch = {
                        keyboard?.hide()
                        if (viewModel.query.isEmpty()) {
                            viewModel.getAllUsers()
                        } else {
                            viewModel.findSearchUsers(viewModel.query)
                        }
                    },
                    navigateToDetail = navigateToDetail,
                    navigateToFavorite = navigateToFavorite
                )
            }
            is UiState.Error -> {
                Toast.makeText(LocalContext.current, uiState.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
fun HomeContent(
    favUser: List<UserResponse>,
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToFavorite: () -> Unit
) {
    val listState = rememberLazyListState()
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = stringResource(R.string.app_name), modifier = modifier
                .align(Alignment.TopStart)
                .padding(16.dp))
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = stringResource(R.string.favorite_button),
                modifier = modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .clickable { navigateToFavorite() }
                    .semantics(mergeDescendants = true) {
                        contentDescription = "Favorite Icon"
                    }
            )
        }
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch
        )
        if (favUser.isEmpty()) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                text = stringResource(id = R.string.list_is_empty),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(bottom = 10.dp),
                modifier = modifier.testTag("UserList")
            ) {
                items(favUser, key = {it.id!!}) {data->
                    UserListItem(
                        username = data.username.toString(),
                        avatarUrl = data.avatarUrl.toString(),
                        modifier = modifier
                            .clickable {
                                navigateToDetail(data.username.toString())
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }

    }

}