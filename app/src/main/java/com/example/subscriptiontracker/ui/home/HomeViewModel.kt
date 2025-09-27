package com.example.subscriptiontracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subscriptiontracker.data.CategoryRepository
import com.example.subscriptiontracker.data.Subscription
import com.example.subscriptiontracker.data.SubscriptionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(
    subscriptionRepository: SubscriptionRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        combine(
            subscriptionRepository.getAllSubscriptions(),
            categoryRepository.getAllCategories()
        ) { subscriptions, categories ->
            val monthlyCost = subscriptions.filter { it.billingCycle.equals("monthly", ignoreCase = true) }.sumOf { it.cost } +
                    subscriptions.filter { it.billingCycle.equals("yearly", ignoreCase = true) }.sumOf { it.cost } / 12
            val yearlyCost = subscriptions.filter { it.billingCycle.equals("yearly", ignoreCase = true) }.sumOf { it.cost } +
                    subscriptions.filter { it.billingCycle.equals("monthly", ignoreCase = true) }.sumOf { it.cost } * 12

            val categoryCosts = subscriptions.groupBy { it.categoryId }
                .mapKeys { entry -> categories.find { it.id == entry.key }?.name ?: "Uncategorized" }
                .mapValues { entry -> entry.value.sumOf { it.cost } }

            HomeUiState(
                subscriptionList = subscriptions,
                totalMonthlyCost = monthlyCost,
                totalYearlyCost = yearlyCost,
                categoryCosts = categoryCosts
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(
    val subscriptionList: List<Subscription> = listOf(),
    val totalMonthlyCost: Double = 0.0,
    val totalYearlyCost: Double = 0.0,
    val categoryCosts: Map<String, Double> = emptyMap()
)