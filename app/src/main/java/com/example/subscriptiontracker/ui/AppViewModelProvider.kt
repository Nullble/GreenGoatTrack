package com.example.subscriptiontracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.subscriptiontracker.SubscriptionTrackerApplication

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for AddEditSubscriptionViewModel
        initializer {
            AddEditSubscriptionViewModel(
                subscriptionTrackerApplication().container.subscriptionRepository,
                subscriptionTrackerApplication().container.categoryRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                subscriptionTrackerApplication().container.subscriptionRepository,
                subscriptionTrackerApplication().container.categoryRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [SubscriptionTrackerApplication].
 */
fun CreationExtras.subscriptionTrackerApplication(): SubscriptionTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SubscriptionTrackerApplication)