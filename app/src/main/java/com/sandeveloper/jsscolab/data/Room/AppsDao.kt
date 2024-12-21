package com.sandeveloper.jsscolab.data.Room

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sandeveloper.jsscolab.domain.Modules.Post.App
import com.sandeveloper.jsscolab.domain.Modules.app.AppEntity

@Dao
interface AppsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(appEntities: List<AppEntity>)

    @Query("SELECT * from apps")
    suspend fun getApps():List<AppEntity>

    @Query("UPDATE apps SET logo =:logo where _id=:id ")
    suspend fun updateLogo(id:String,logo:String)

    @Query("SELECT _id, name,logo from apps where category=:category")
    suspend fun getAppsByCategory(category:String):List<App>

    @Query("SELECT category from apps")
    suspend fun getCategories():List<String>

    @Query("SELECT name from apps where _id=:id")
    suspend fun getAppNameById(id:String):String


}