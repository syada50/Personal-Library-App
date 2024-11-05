package com.example.personallibrary

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personallibrary.data.Book
import com.example.personallibrary.databinding.ActivityMainBinding
import com.example.personallibrary.viewmodel.BookViewModel
import com.example.personallibrary.viewmodel.BookViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import android.widget.EditText
import android.widget.CheckBox

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookViewModel: BookViewModel
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize BookViewModel using Application context
        bookViewModel = ViewModelProvider(this, BookViewModelFactory(application))
            .get(BookViewModel::class.java)

        setupRecyclerView()
        setupFab()
        observeBooks()
    }

    private fun setupRecyclerView() {
        adapter = BookAdapter { book -> editBook(book) }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val book = adapter.currentList[viewHolder.adapterPosition]
                bookViewModel.delete(book)
                Snackbar.make(binding.root, getString(R.string.book_deleted), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) {
                        bookViewModel.insert(book)
                        adapter.updateItem(book)
                    }.show()
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            showAddEditDialog()
        }
    }

    private fun observeBooks() {
        bookViewModel.allBooks.observe(this) { books ->
            adapter.submitList(books)
        }
    }

    private fun showAddEditDialog(book: Book? = null) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_book, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val authorEditText = dialogView.findViewById<EditText>(R.id.editTextAuthor)
        val pagesEditText = dialogView.findViewById<EditText>(R.id.editTextPages)
        val isReadCheckBox = dialogView.findViewById<CheckBox>(R.id.checkBoxIsRead)

        if (book != null) {
            titleEditText.setText(book.title)
            authorEditText.setText(book.author)
            pagesEditText.setText(book.pages.toString())
            isReadCheckBox.isChecked = book.isRead
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(if (book == null) getString(R.string.add_book) else getString(R.string.edit_book))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val title = titleEditText.text.toString()
                val author = authorEditText.text.toString()
                val pages = pagesEditText.text.toString().toIntOrNull() ?: 0
                val isRead = isReadCheckBox.isChecked

                if (title.isNotBlank() && author.isNotBlank() && pages > 0) {
                    if (book == null) {
                        val newBook = Book(title = title, author = author, pages = pages, isRead = isRead)
                        bookViewModel.insert(newBook)
                    } else {
                        book.title = title
                        book.author = author
                        book.pages = pages
                        book.isRead = isRead
                        bookViewModel.update(book)
                        adapter.updateItem(book)
                    }
                } else {
                    Snackbar.make(binding.root, getString(R.string.fill_all_fields), Snackbar.LENGTH_LONG).show()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun editBook(book: Book) {
        showAddEditDialog(book)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchBooks(newText)
                }
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_title -> {
                sortBooks { it.title }
                true
            }
            R.id.action_sort_author -> {
                sortBooks { it.author }
                true
            }
            R.id.action_sort_pages -> {
                sortBooks { it.pages }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun searchBooks(query: String) {
        bookViewModel.searchBooks(query).observe(this) { books ->
            adapter.submitList(books)
        }
    }

    private fun <T : Comparable<T>> sortBooks(selector: (Book) -> T) {
        bookViewModel.allBooks.value?.let { books ->
            adapter.submitList(books.sortedBy(selector))
        }
    }
}
