package com.sandeveloper.jsscolab.domain.Modules.swap

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "swapItems")
data class SwapEntity(
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val name: String,
    val branch: String,
    val semester: Int
)
