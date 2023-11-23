package com.mazzampr.githubcompose.ui.screen.favorite

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mazzampr.githubcompose.R
import com.mazzampr.githubcompose.data.local.entity.UsersEntity
import com.mazzampr.githubcompose.di.Injection
import com.mazzampr.githubcompose.ui.ViewModelFactory
import com.mazzampr.githubcompose.ui.common.UiState
import com.mazzampr.githubcompose.ui.components.UserListItem

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    navigateToDetail: (String) -> Unit,
    navigateBack: () -> Unit
)  {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let {
        when(it) {
            is UiState.Loading -> {
                viewModel.getAllFavUsers()
                Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            is UiState.Success -> {
                FavoriteContent(
                    favUser = it.data,
                    navigateToDetail = navigateToDetail,
                    navigateBack = navigateBack,
                    modifier = modifier
                )
            }
            is UiState.Error -> {
                Toast.makeText(LocalContext.current, it.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
fun FavoriteContent(
    favUser: List<UsersEntity>,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val listState = rememberLazyListState()
    Column(
        modifier = modifier
    ) {
        Box {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                modifier = modifier
                    .clickable { navigateBack() }
                    .padding(16.dp)
            )
            Text(
                text = stringResource(id = R.string.favorite_users),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
        }
        if (favUser.isEmpty()) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = stringResource(id = R.string.list_is_empty),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(bottom = 10.dp)
            ) {
                items(favUser, key = {it.idUser}) {data->
                    UserListItem(
                        username = data.username,
                        avatarUrl = data.avatarUrl,
                        modifier = Modifier
                            .clickable {
                                navigateToDetail(data.username)
                            }
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }

}