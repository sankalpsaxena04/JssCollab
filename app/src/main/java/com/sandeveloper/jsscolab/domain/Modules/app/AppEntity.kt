package com.sandeveloper.jsscolab.domain.Modules.app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("apps")
data class AppEntity (
    @PrimaryKey(autoGenerate = false)
    val _id:String,
    val name:String,
    val logo:String,
    val category:String

)