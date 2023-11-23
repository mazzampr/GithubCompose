package com.mazzampr.githubcompose.ui.screen.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mazzampr.githubcompose.R
import com.mazzampr.githubcompose.data.local.entity.UsersEntity
import com.mazzampr.githubcompose.data.remote.response.UserResponse
import com.mazzampr.githubcompose.di.Injection
import com.mazzampr.githubcompose.ui.ViewModelFactory
import com.mazzampr.githubcompose.ui.common.UiState
import com.mazzampr.githubcompose.ui.theme.GithubComposeTheme

@Composable
fun DetailScreen(
    username: String,
    viewModel: DetailUserViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository(LocalContext.current)
        )
    ),
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val userFavDetail by viewModel.getUserFavDetail(username)
        .collectAsState(initial = emptyList())

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let {uiState->
        when(uiState) {
            is UiState.Loading -> {
                viewModel.getDetailUser(username)
                Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            is UiState.Success -> {
                val data = uiState.data
                val userEntity = UsersEntity(data.username!!, data.id.toString(), data.avatarUrl!!)
                DetailContent(
                    data,
                    onFavoriteButtonClicked = {
                        if (userFavDetail.isNotEmpty()) {
                            // User is already a favorite, perform unfavorite action
                            viewModel.deleteFavorite(userEntity)
                            Toast.makeText(context, "User deleted from Favorite!", Toast.LENGTH_SHORT).show()
                        } else {
                            // User is not a favorite, perform favorite action
                            viewModel.insertFavorite(userEntity)
                            Toast.makeText(context, "User Added to Favorite!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    isFavorite = userFavDetail.isNotEmpty(),
                    onBackClick = navigateBack
                )
            }
            is UiState.Error -> {
                Text(text = uiState.errorMessage, modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth())
            }
        }
    }
}

@Composable
fun DetailContent(
    user: UserResponse,
    onBackClick: () -> Unit,
    onFavoriteButtonClicked: () -> Unit,
    isFavorite: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
                .fillMaxWidth()
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .padding(top = 80.dp)
                        .width(180.dp)
                        .height(180.dp)
                        .wrapContentWidth(CenterHorizontally)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = modifier
                        .clickable { onBackClick() }
                        .padding(16.dp)
                )
                FavoriteButton(
                    onClick = {
                        onFavoriteButtonClicked()
                    },
                    isFavorite = isFavorite,
                    modifier = modifier
                        .align(TopEnd)
                        .padding(16.dp)
                        .semantics(mergeDescendants = true) {
                            contentDescription = "Favorite Button"
                        }
                )
            }
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = modifier.padding(16.dp)
            ) {
                Text(
                    text = user.username!!,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = modifier.fillMaxWidth()
                )
                Row(modifier = modifier.padding(top = 16.dp)) {
                    Text(
                        text = "${user.followers} Followers",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = "${user.following} Following",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteButton(
    onClick: () -> Unit,
    isFavorite: Boolean,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = { onClick() },
        modifier = modifier.width(44.dp).height(44.dp)
    ) {
        Icon(
            imageVector = if (isFavorite) { Icons.Default.Favorite } else { Icons.Default.FavoriteBorder },
            contentDescription = stringResource(R.string.favorite_button),
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun showDetailPreview() {
    GithubComposeTheme {
        DetailContent(
            user = UserResponse(
                username = "Azzam",
                id = 1,
                avatarUrl = "https://avatars.githubusercontent.com/u/24699504?v=4",
                followers = 10,
                following = 10
            ),
            onBackClick = { /*TODO*/ },
            onFavoriteButtonClicked = { /*TODO*/ }, isFavorite = false)
    }
}
