package com.example.subscriptiontracker.ui.subscription

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.subscriptiontracker.R
import com.example.subscriptiontracker.data.Category
import com.example.subscriptiontracker.ui.AppViewModelProvider
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSubscriptionScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditSubscriptionViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_subscription_title)) },
            )
        },
        modifier = modifier
    ) { innerPadding ->
        SubscriptionEntryBody(
            subscriptionUiState = viewModel.subscriptionUiState,
            categories = viewModel.categories,
            onSubscriptionValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveSubscription()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SubscriptionEntryBody(
    subscriptionUiState: SubscriptionUiState,
    categories: List<Category>,
    onSubscriptionValueChange: (SubscriptionUiState) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SubscriptionInputForm(
            subscriptionUiState = subscriptionUiState,
            categories = categories,
            onValueChange = onSubscriptionValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = subscriptionUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionInputForm(
    subscriptionUiState: SubscriptionUiState,
    categories: List<Category>,
    modifier: Modifier = Modifier,
    onValueChange: (SubscriptionUiState) -> Unit = {},
    enabled: Boolean = true
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = subscriptionUiState.name,
            onValueChange = { onValueChange(subscriptionUiState.copy(name = it)) },
            label = { Text(stringResource(R.string.subscription_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = subscriptionUiState.cost,
            onValueChange = { onValueChange(subscriptionUiState.copy(cost = it)) },
            label = { Text(stringResource(R.string.subscription_cost_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = subscriptionUiState.billingCycle,
            onValueChange = { onValueChange(subscriptionUiState.copy(billingCycle = it)) },
            label = { Text(stringResource(R.string.subscription_billing_cycle_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(subscriptionUiState.firstPaymentDate),
            onValueChange = { },
            label = { Text(stringResource(R.string.first_payment_date)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            enabled = false, // Disable direct text input
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = categories.find { it.id == subscriptionUiState.categoryId }?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.category)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onValueChange(subscriptionUiState.copy(categoryId = category.id))
                            expanded = false
                        }
                    )
                }
            }
        }
        OutlinedTextField(
            value = subscriptionUiState.paymentMethod,
            onValueChange = { onValueChange(subscriptionUiState.copy(paymentMethod = it)) },
            label = { Text(stringResource(R.string.payment_method)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = subscriptionUiState.notes,
            onValueChange = { onValueChange(subscriptionUiState.copy(notes = it)) },
            label = { Text(stringResource(R.string.notes)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onValueChange(subscriptionUiState.copy(firstPaymentDate = Date(it)))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}