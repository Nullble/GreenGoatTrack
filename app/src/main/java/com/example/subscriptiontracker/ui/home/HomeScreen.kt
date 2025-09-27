package com.example.subscriptiontracker.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.subscriptiontracker.R
import com.example.subscriptiontracker.data.Subscription
import com.example.subscriptiontracker.ui.AppViewModelProvider
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSubscriptionEntry: () -> Unit,
    navigateToSubscriptionUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToSubscriptionEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_subscription_title)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            homeUiState = homeUiState,
            onSubscriptionClick = navigateToSubscriptionUpdate,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    homeUiState: HomeUiState,
    onSubscriptionClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        DashboardSummaryCard(
            totalMonthlyCost = homeUiState.totalMonthlyCost,
            totalYearlyCost = homeUiState.totalYearlyCost,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        CategoryBreakdownCard(
            categoryCosts = homeUiState.categoryCosts,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        if (homeUiState.subscriptionList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_subscriptions_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            SubscriptionList(
                subscriptionList = homeUiState.subscriptionList,
                onSubscriptionClick = { onSubscriptionClick(it.id) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun DashboardSummaryCard(
    totalMonthlyCost: Double,
    totalYearlyCost: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.dashboard_summary_title),
                style = MaterialTheme.typography.titleLarge
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.total_monthly_cost),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = NumberFormat.getCurrencyInstance().format(totalMonthlyCost),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.total_yearly_cost),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = NumberFormat.getCurrencyInstance().format(totalYearlyCost),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun CategoryBreakdownCard(
    categoryCosts: Map<String, Double>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.category_breakdown_title),
                style = MaterialTheme.typography.titleLarge
            )
            if (categoryCosts.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_category_data),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                categoryCosts.forEach { (category, cost) ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = NumberFormat.getCurrencyInstance().format(cost),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun SubscriptionList(
    subscriptionList: List<Subscription>,
    onSubscriptionClick: (Subscription) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = subscriptionList, key = { it.id }) { item ->
            SubscriptionCard(
                subscription = item,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onSubscriptionClick(item) }
            )
        }
    }
}

@Composable
private fun SubscriptionCard(subscription: Subscription, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = subscription.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "$${subscription.cost}", // Basic formatting
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = subscription.billingCycle,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}