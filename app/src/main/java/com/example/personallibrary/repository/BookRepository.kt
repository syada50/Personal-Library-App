package com.example.personallibrary.repository



import androidx.lifecycle.LiveData
import com.example.personallibrary.data.Book
import com.example.personallibrary.data.BookDao

class BookRepository(private val bookDao: BookDao) {
    val allBooks: LiveData<List<Book>> = bookDao.getAllBooks()

    suspend fun insert(book: Book) {
        bookDao.insertBook(book)
    }

    suspend fun update(book: Book) {
        bookDao.updateBook(book)
    }

    suspend fun delete(book: Book) {
        bookDao.deleteBook(book)
    }

    fun searchBooks(query: String): LiveData<List<Book>> {
        return bookDao.searchBooks("%$query%")
    }
}