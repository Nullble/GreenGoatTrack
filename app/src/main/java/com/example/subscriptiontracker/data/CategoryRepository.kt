package com.example.subscriptiontracker.data

import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }

    suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }
}