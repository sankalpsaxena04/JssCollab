package com.sandeveloper.jsscolab.data.Room

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import com.sandeveloper.jsscolab.domain.Modules.app.AppEntity

@Dao
interface AppsDao {
    @Insert
    suspend fun insertApps(appEntities: List<AppEntity>)

    @Query("SELECT * from apps")
    suspend fun getApps():List<AppEntity>

    @Query("UPDATE apps SET logo =:logo where id=:id ")
    suspend fun updateLogo(id:String,logo:String)


}