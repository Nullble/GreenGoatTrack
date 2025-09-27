package com.example.subscriptiontracker

import android.app.Application
import com.example.subscriptiontracker.data.AppDatabase
import com.example.subscriptiontracker.data.CategoryRepository
import com.example.subscriptiontracker.data.SubscriptionRepository

/**
 * The Application class for the Subscription Tracker app.
 * This class holds the application-level dependency container.
 */
class SubscriptionTrackerApplication : Application() {
    /**
     * AppContainer instance used by the rest of the classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}

/**
 * A container for dependencies that are shared across the application.
 */
interface AppContainer {
    val subscriptionRepository: SubscriptionRepository
    val categoryRepository: CategoryRepository
}

/**
 * Implementation of [AppContainer] that provides instances of repositories.
 */
private class AppContainerImpl(private val context: Application) : AppContainer {
    override val subscriptionRepository: SubscriptionRepository by lazy {
        SubscriptionRepository(AppDatabase.getDatabase(context).subscriptionDao())
    }

    override val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(AppDatabase.getDatabase(context).categoryDao())
    }
}