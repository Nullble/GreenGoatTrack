package com.example.subscriptiontracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscription: Subscription)

    @Update
    suspend fun update(subscription: Subscription)

    @Delete
    suspend fun delete(subscription: Subscription)

    @Query("SELECT * FROM subscriptions WHERE id = :id")
    fun getSubscription(id: Int): Flow<Subscription>

    @Query("SELECT * FROM subscriptions ORDER BY name ASC")
    fun getAllSubscriptions(): Flow<List<Subscription>>
}