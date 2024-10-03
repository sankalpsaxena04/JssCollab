package com.sandeveloper.jsscolab.data.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity

@Dao
interface SwapDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSwaps(swaps: List<SwapEntity>)

    @Query("SELECT * FROM swapItems")
    suspend fun getSwaps(): List<SwapEntity>

    @Query("DELETE FROM SwapItems")
    suspend fun deleteAllSwapItems()

    @Query("SELECT * FROM swapItems WHERE name LIKE '%' || :query || '%' OR branch LIKE '%' || :query || '%' OR semester LIKE '%' || :query || '%'")
    fun searchSwapItems(query: String): LiveData<List<SwapEntity>>
    @Query("SELECT * FROM swapItems WHERE name LIKE '%' || :query || '%' OR branch LIKE '%' || :query || '%' OR semester LIKE '%' || :query || '%'")
    fun searchgiveSwapItems(query: String): LiveData<List<SwapEntity>>

    @Query("SELECT name FROM swapItems WHERE _id = :id")
    fun getNames(id: String): String

}