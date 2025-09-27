package com.example.subscriptiontracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.subscriptiontracker.ui.home.HomeScreen
import com.example.subscriptiontracker.ui.subscription.AddEditSubscriptionScreen

/**
 * Sealed class to define the navigation destinations.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddSubscription : Screen("add_subscription")
    // TODO: Add route for editing a subscription
}

@Composable
fun SubscriptionNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                navigateToSubscriptionEntry = { navController.navigate(Screen.AddSubscription.route) },
                navigateToSubscriptionUpdate = {
                    // TODO: Implement navigation to edit screen
                }
            )
        }
        composable(route = Screen.AddSubscription.route) {
            AddEditSubscriptionScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}