package org.example.project.core.data.datastore

import kotlinx.coroutines.flow.Flow

interface DataStorePreferences {

    suspend fun getString(key: String, defaultValue: String = ""): String
    fun getStringFlow(key: String, defaultValue: String = ""): Flow<String>
    suspend fun setString(key: String, value: String)

    suspend fun getInt(key: String, defaultValue: Int = 0): Int
    fun getIntFlow(key: String, defaultValue: Int = 0): Flow<Int>
    suspend fun setInt(key: String, value: Int)

    suspend fun getFloat(key: String, defaultValue: Float = 0f): Float
    fun getFloatFlow(key: String, defaultValue: Float = 0f): Flow<Float>
    suspend fun setFloat(key: String, value: Float)

    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun getBooleanFlow(key: String, defaultValue: Boolean? = null): Flow<Boolean?>
    suspend fun setBoolean(key: String, value: Boolean)

    suspend fun getLong(key: String, defaultValue: Long = 0L): Long
    fun getLongFlow(key: String, defaultValue: Long = 0L): Flow<Long>
    suspend fun setLong(key: String, value: Long)

    suspend fun remove(key: String)
    suspend fun clear()
}
