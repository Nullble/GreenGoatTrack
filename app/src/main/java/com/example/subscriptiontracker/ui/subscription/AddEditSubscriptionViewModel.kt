package com.example.subscriptiontracker.ui.subscription

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subscriptiontracker.data.Category
import com.example.subscriptiontracker.data.CategoryRepository
import com.example.subscriptiontracker.data.Subscription
import com.example.subscriptiontracker.data.SubscriptionRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date

/**
 * ViewModel to validate and insert subscriptions.
 */
class AddEditSubscriptionViewModel(
    private val subscriptionRepository: SubscriptionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    /**
     * Holds current subscription ui state
     */
    var subscriptionUiState by mutableStateOf(SubscriptionUiState())
        private set

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categoryList ->
                categories = categoryList
            }
        }
    }

    /**
     * Updates the [subscriptionUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(newSubscriptionUiState: SubscriptionUiState) {
        subscriptionUiState = newSubscriptionUiState.copy(isEntryValid = validateInput(newSubscriptionUiState))
    }

    suspend fun saveSubscription() {
        if (validateInput()) {
            subscriptionRepository.insert(subscriptionUiState.toSubscription())
        }
    }

    private fun validateInput(uiState: SubscriptionUiState = subscriptionUiState): Boolean {
        return with(uiState) {
            name.isNotBlank() && cost.isNotBlank() && billingCycle.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for a Subscription.
 */
data class SubscriptionUiState(
    val id: Int = 0,
    val name: String = "",
    val cost: String = "",
    val billingCycle: String = "",
    val firstPaymentDate: Date = Date(),
    val categoryId: Int = 0,
    val paymentMethod: String = "",
    val notes: String = "",
    val isEntryValid: Boolean = false
)

/**
 * Extension function to convert [SubscriptionUiState] to [Subscription].
 */
fun SubscriptionUiState.toSubscription(): Subscription = Subscription(
    id = id,
    name = name,
    cost = cost.toDoubleOrNull() ?: 0.0,
    billingCycle = billingCycle,
    firstPaymentDate = firstPaymentDate,
    categoryId = categoryId,
    paymentMethod = paymentMethod,
    notes = notes
)