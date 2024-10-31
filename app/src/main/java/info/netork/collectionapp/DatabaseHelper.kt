package info.netork.collectionapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

import info.netork.collectionapp.data.CollectionItem

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "CollectionDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "collections"

        private const val COLUMN_ID = "id"
        private const val COLUMN_SN = "sn"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_VOUCHER_CODE = "voucher_code"
        private const val COLUMN_AMOUNT = "amount"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SN TEXT NOT NULL,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_VOUCHER_CODE TEXT NOT NULL,
                $COLUMN_AMOUNT REAL NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTable)
        db.execSQL("CREATE INDEX idx_date ON $TABLE_NAME($COLUMN_DATE)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // In a real app, you'd want to implement a proper upgrade strategy
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertItem(item: CollectionItem): Long {
        val db = this.writableDatabase
        var insertedId: Long = -1
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(COLUMN_SN, item.sn)
                put(COLUMN_NAME, item.name)
                put(COLUMN_DATE, item.date)
                put(COLUMN_VOUCHER_CODE, item.voucherCode)
                put(COLUMN_AMOUNT, item.amount)
            }
            insertedId = db.insert(TABLE_NAME, null, values)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error inserting item", e)
        } finally {
            db.endTransaction()
        }
        return insertedId
    }

    fun getAllItems(): Flow<List<CollectionItem>> = flow {
        val items = mutableListOf<CollectionItem>()
        val db = this@DatabaseHelper.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_DATE DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val item = CollectionItem(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    sn = it.getString(it.getColumnIndexOrThrow(COLUMN_SN)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    date = it.getString(it.getColumnIndexOrThrow(COLUMN_DATE)),
                    voucherCode = it.getString(it.getColumnIndexOrThrow(COLUMN_VOUCHER_CODE)),
                    amount = it.getDouble(it.getColumnIndexOrThrow(COLUMN_AMOUNT))
                )
                items.add(item)
            }
        }
        emit(items)
    }

    fun updateItem(item: CollectionItem): Int {
        val db = this.writableDatabase
        var updatedRows = 0
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(COLUMN_SN, item.sn)
                put(COLUMN_NAME, item.name)
                put(COLUMN_DATE, item.date)
                put(COLUMN_VOUCHER_CODE, item.voucherCode)
                put(COLUMN_AMOUNT, item.amount)
            }
            updatedRows = db.update(
                TABLE_NAME,
                values,
                "$COLUMN_ID = ?",
                arrayOf(item.id.toString())
            )
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error updating item", e)
        } finally {
            db.endTransaction()
        }
        return updatedRows
    }

    fun deleteItem(id: Int): Int {
        val db = this.writableDatabase
        var deletedRows = 0
        db.beginTransaction()
        try {
            deletedRows = db.delete(
                TABLE_NAME,
                "$COLUMN_ID = ?",
                arrayOf(id.toString())
            )
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error deleting item", e)
        } finally {
            db.endTransaction()
        }
        return deletedRows
    }

    fun deleteAllItems() {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            db.delete(TABLE_NAME, null, null)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error deleting all items", e)
        } finally {
            db.endTransaction()
        }
    }

    fun backup(backupPath: String) {
        try {
            val currentDB = this.readableDatabase.path
            val source = File(currentDB)
            val destination = File(backupPath)
            source.copyTo(destination, overwrite = true)
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error backing up database", e)
        }
    }

    fun restore(backupPath: String) {
        try {
            close()
            val currentDB = this.readableDatabase.path
            val source = File(backupPath)
            val destination = File(currentDB)
            source.copyTo(destination, overwrite = true)
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error restoring database", e)
        }
    }
}