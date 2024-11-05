package com.example.personallibrary


import android.app.Application
import com.example.personallibrary.data.BookDatabase

class PersonalLibraryApplication : Application() {
    val database: BookDatabase by lazy { BookDatabase.getDatabase(this) }
}