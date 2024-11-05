package com.example.personallibrary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.personallibrary.data.Book
import com.example.personallibrary.data.BookDatabase
import com.example.personallibrary.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository
    val allBooks: LiveData<List<Book>>

    init {
        // Access the database instance and initialize the repository
        val bookDao = BookDatabase.getDatabase(application).bookDao()
        repository = BookRepository(bookDao)
        allBooks = repository.allBooks
    }

    fun insert(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(book)
    }

    fun update(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(book)
    }

    fun delete(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(book)
    }

    fun searchBooks(query: String): LiveData<List<Book>> {
        return repository.searchBooks(query)
    }
}
