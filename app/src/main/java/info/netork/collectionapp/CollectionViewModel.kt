// CollectionViewModel.kt
package info.netork.collectionapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import info.netork.collectionapp.data.CollectionItem
import info.netork.collectionapp.data.DatabaseHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CollectionViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = DatabaseHelper(application)
    val allItems: Flow<List<CollectionItem>> = dbHelper.getAllItems()

    fun addItem(item: CollectionItem) {
        viewModelScope.launch {
            dbHelper.insertItem(item)
        }
    }

    fun updateItem(item: CollectionItem) {
        viewModelScope.launch {
            dbHelper.updateItem(item)
        }
    }

    fun deleteItem(item: CollectionItem) {
        viewModelScope.launch {
            dbHelper.deleteItem(item.id)
        }
    }

    fun backupDatabase() {
        viewModelScope.launch {
            val backupPath = getApplication<Application>().getExternalFilesDir(null)?.absolutePath +
                    "/collection_backup.db"
            dbHelper.backup(backupPath)
        }
    }

    fun restoreDatabase() {
        viewModelScope.launch {
            val backupPath = getApplication<Application>().getExternalFilesDir(null)?.absolutePath +
                    "/collection_backup.db"
            dbHelper.restore(backupPath)
        }
    }

    override fun onCleared() {
        dbHelper.close()
        super.onCleared()
    }
}