package com.mazzampr.githubcompose

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.testing.TestNavHostController
import com.mazzampr.githubcompose.ui.theme.GithubComposeTheme
import androidx.navigation.compose.ComposeNavigator
import com.mazzampr.githubcompose.ui.navigation.Screen
import com.mazzampr.githubcompose.utils.assertCurrentRouteName
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class JetGithubAppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            GithubComposeTheme{
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                JetGithubApp(navController = navController)
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
       navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_clickListItem_navigateToDetailWithData() {
        runBlocking {
            delay(5000)
            composeTestRule.onNodeWithTag("UserList").performScrollToIndex(10)
            composeTestRule.onNodeWithText("kevinclark").performClick()
            navController.assertCurrentRouteName(Screen.DetailUser.route)
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithText("kevinclark").assertIsDisplayed()
        }
    }

    @Test
    fun navHost_clickListItem_navigatesBack() {
        runBlocking {
            navController.assertCurrentRouteName(Screen.Home.route)
            delay(5000)
            composeTestRule.onNodeWithTag("UserList").performScrollToIndex(10)
            composeTestRule.onNodeWithText("kevinclark").performClick()
            navController.assertCurrentRouteName(Screen.DetailUser.route)
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back)).performClick()
            navController.assertCurrentRouteName(Screen.Home.route)
        }

    }

    @Test
    fun navHost_bottomNavigation_working() {
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.menu_profile)).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.menu_home)).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_clickFavoriteButton_navigateToFavorite() {
        runBlocking {
            navController.assertCurrentRouteName(Screen.Home.route)
            delay(5000)
            composeTestRule.onNodeWithContentDescription("Favorite Icon").performClick()
            navController.assertCurrentRouteName(Screen.Favorite.route)
        }

    }

    @Test
    fun navHost_clickButtonFavoriteInDetailScreen_navigateToFavoriteWithData() {
        runBlocking {
            delay(5000)
            composeTestRule.onNodeWithTag("UserList").performScrollToIndex(10)
            composeTestRule.onNodeWithText("kevinclark").performClick()
            navController.assertCurrentRouteName(Screen.DetailUser.route)
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription("Favorite Button").performClick()
            composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back)).performClick()
            navController.assertCurrentRouteName(Screen.Home.route)
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription("Favorite Icon").performClick()
            navController.assertCurrentRouteName(Screen.Favorite.route)
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithText("kevinclark").assertIsDisplayed()
        }

    }
}