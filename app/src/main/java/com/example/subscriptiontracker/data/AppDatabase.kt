package com.example.subscriptiontracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(entities = [Subscription::class, Category::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "subscription_database"
                )
                    .addCallback(AppDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val context: Context,
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                    populateDatabase(database.categoryDao())
                }
            }
        }

        suspend fun populateDatabase(categoryDao: CategoryDao) {
            // Add default categories.
            categoryDao.insert(Category(name = "Entertainment"))
            categoryDao.insert(Category(name = "Utilities"))
            categoryDao.insert(Category(name = "Work"))
            categoryDao.insert(Category(name = "Health"))
            categoryDao.insert(Category(name = "Education"))
            categoryDao.insert(Category(name = "Shopping"))
        }
    }
}