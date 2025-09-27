package com.example.subscriptiontracker.data

import kotlinx.coroutines.flow.Flow

class SubscriptionRepository(private val subscriptionDao: SubscriptionDao) {

    fun getAllSubscriptions(): Flow<List<Subscription>> {
        return subscriptionDao.getAllSubscriptions()
    }

    fun getSubscription(id: Int): Flow<Subscription> {
        return subscriptionDao.getSubscription(id)
    }

    suspend fun insert(subscription: Subscription) {
        subscriptionDao.insert(subscription)
    }

    suspend fun update(subscription: Subscription) {
        subscriptionDao.update(subscription)
    }

    suspend fun delete(subscription: Subscription) {
        subscriptionDao.delete(subscription)
    }
}