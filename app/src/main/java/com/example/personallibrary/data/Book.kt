package com.example.personallibrary.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var author: String,
    var pages: Int,
    var isRead: Boolean = false
)
