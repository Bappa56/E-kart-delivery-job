package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DeliveryApplicationDao {

    @Query("SELECT * FROM delivery_applications ORDER BY submittedAt DESC")
    fun getAllApplications(): Flow<List<DeliveryApplication>>

    @Query("SELECT * FROM delivery_applications WHERE applicationId = :applicationId LIMIT 1")
    fun getApplicationById(applicationId: String): Flow<DeliveryApplication?>

    @Query("SELECT * FROM delivery_applications WHERE applicationId = :query OR mobile = :query ORDER BY submittedAt DESC")
    fun searchByMobileOrId(query: String): Flow<List<DeliveryApplication>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: DeliveryApplication)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplications(applications: List<DeliveryApplication>)

    @Update
    suspend fun updateApplication(application: DeliveryApplication)

    @Query("SELECT COUNT(*) FROM delivery_applications")
    fun getTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM delivery_applications WHERE status = 'PENDING'")
    fun getPendingCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM delivery_applications WHERE status = 'APPROVED'")
    fun getApprovedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM delivery_applications WHERE status = 'REJECTED'")
    fun getRejectedCount(): Flow<Int>
}
