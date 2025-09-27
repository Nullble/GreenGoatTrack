package com.example.subscriptiontracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "subscriptions")
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val cost: Double,
    val billingCycle: String, // e.g., "Monthly", "Yearly", or a custom cron-like string
    val firstPaymentDate: Date,
    val categoryId: Int, // Foreign key to the Category table
    val paymentMethod: String,
    val notes: String? = null
)