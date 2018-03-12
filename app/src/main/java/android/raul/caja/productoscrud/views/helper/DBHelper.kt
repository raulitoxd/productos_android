package android.raul.caja.productoscrud.views.helper

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.raul.caja.productoscrud.model.Producto
import android.util.Log

class DBHelper(context: Context?) : SQLiteOpenHelper(context, "Producto.db", null, 1) {

    private val TAG: String? = DBHelper::class.simpleName
    private val TABLE_NAME: String? = "Producto"
    private val COD_PROD: String? = "codProd"
    private val NOMBRE_PROD: String? = "NombreProd"
    private val DESCRIPCION: String? = "Descripcion"
    private val CANTIDAD: String? = "cantidad"

    override fun onCreate(sqliteDatabase: SQLiteDatabase?) {
        val queryCreateTable = "" +
                "create table " + TABLE_NAME + "" +
                "(" +
                "" + COD_PROD + " integer primary key, " +
                "" + NOMBRE_PROD + " varchar(20) not null, " +
                "" + DESCRIPCION + " varchar(100) not null, " +
                "" + CANTIDAD + " integer not null " +
                ")"
        println(queryCreateTable)
        sqliteDatabase?.execSQL(queryCreateTable)
    }

    override fun onUpgrade(sqliteDatabase: SQLiteDatabase?, newVersion: Int, oldVersion: Int) {
        val queryUpgradeTable = "" +
                "drop table if exists " + TABLE_NAME
        sqliteDatabase?.execSQL(queryUpgradeTable)
        onCreate(sqliteDatabase)
    }

    fun insertDataContentValues(producto: Producto): Boolean {
        val sqliteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COD_PROD, producto.codProd)
        contentValues.put(NOMBRE_PROD, producto.NombreProd)
        contentValues.put(DESCRIPCION, producto.Descripcion)
        contentValues.put(CANTIDAD, producto.cantidad)

        sqliteDatabase?.insert(TABLE_NAME, null, contentValues)
        sqliteDatabase?.close()
        return true
    }

    fun insertData(producto: Producto): Boolean {
        val sqliteDatabase = writableDatabase
        try {
            val query = "insert into $TABLE_NAME " +
                        "values " +
                        "(\"${producto.codProd}\", \"${producto.NombreProd}\", \"${producto.Descripcion}\"," +
                    " \"${producto.cantidad}\")"
            println("insertData " + query)
            sqliteDatabase.execSQL(query)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun insertDataContentValues(nombreProducto: String, descripcion: String, cantidad: Int): Boolean {
        val sqliteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOMBRE_PROD, nombreProducto)
        contentValues.put(DESCRIPCION, descripcion)
        contentValues.put(CANTIDAD, cantidad)
        sqliteDatabase?.insert(TABLE_NAME, null, contentValues)
        sqliteDatabase?.close()
        return true
    }

    fun insertData(nombreProducto: String, descripcion: String, cantidad: Int): Boolean {
        val sqliteDatabase = writableDatabase
        try {
            val query = "insert into $TABLE_NAME" +
                        "values " +
                        "(null, $nombreProducto, $descripcion, $cantidad)"
            sqliteDatabase.execSQL(query)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun isDuplicate(nombreProd: String): Boolean {
        val sqliteDatabase = readableDatabase
        val cursor = sqliteDatabase?.rawQuery(
                "select " + NOMBRE_PROD + " " +
                        "where " + NOMBRE_PROD + " = " +
                        nombreProd,
                null
        )
        cursor?.moveToFirst()
        while (cursor?.isAfterLast?.not() as Boolean) {
            sqliteDatabase.close()
            return true
        }
        sqliteDatabase.close()
        return false
    }

    fun numberOfRows(): Int {
        val sqliteDatabase = readableDatabase
        val numRows = DatabaseUtils.queryNumEntries(sqliteDatabase, TABLE_NAME).toInt()
        sqliteDatabase?.close()
        return numRows
    }

    fun updateData(codProd: String, producto: Producto): Boolean {
        try {
            val sqliteDatabase = writableDatabase
            val contentValues = ContentValues()
            contentValues.put(NOMBRE_PROD, producto.NombreProd)
            contentValues.put(DESCRIPCION, producto.Descripcion)
            contentValues.put(CANTIDAD, producto.cantidad)
            sqliteDatabase?.update(TABLE_NAME, contentValues, COD_PROD + " = ?",
                    arrayOf(producto.codProd.toString()))
            sqliteDatabase?.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun updateData(codProdOld: Int, codProdNew: Int, nombreProd: String, descripcion: String,
                   cantidad: Int): Boolean {
        try {
            val sqliteDatabase = writableDatabase
            val contentValues = ContentValues()
            contentValues.put(COD_PROD, codProdNew)
            contentValues.put(NOMBRE_PROD, nombreProd)
            contentValues.put(DESCRIPCION, descripcion)
            contentValues.put(CANTIDAD, cantidad)
            sqliteDatabase?.update(TABLE_NAME, contentValues, COD_PROD + " = ?",
                    arrayOf(codProdOld.toString()))
            sqliteDatabase?.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun deleteData(producto: Producto): Boolean {
        try {
            val sqliteDatabase = writableDatabase
            sqliteDatabase?.delete(TABLE_NAME, COD_PROD + " = ?",
                    arrayOf(producto.codProd.toString()))
            sqliteDatabase?.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun deleteData(codProd: String): Boolean {
        try {
            val sqliteDatabase = writableDatabase
            sqliteDatabase?.delete(TABLE_NAME, COD_PROD + " = ?", arrayOf(codProd))
            sqliteDatabase?.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun deleteAllData(): Boolean {
        try {
            val sqliteDatabase = writableDatabase
            sqliteDatabase?.execSQL("delete from " + TABLE_NAME)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun getData(nim: String): Producto {
        val sqliteDatabase = readableDatabase
        val queryGetOne = "select * from $TABLE_NAME where $NOMBRE_PROD = $nim"
        val cursor = sqliteDatabase?.rawQuery(queryGetOne, null)
        cursor?.moveToFirst()
        var producto: Producto? = null
        while (cursor?.isAfterLast?.not() as Boolean) {
            producto = Producto(
                    codProd = cursor.getInt(cursor.getColumnIndex(COD_PROD)),
                    NombreProd = cursor.getString(cursor.getColumnIndex(NOMBRE_PROD)),
                    Descripcion = cursor.getString(cursor.getColumnIndex(DESCRIPCION)),
                    cantidad = cursor.getInt(cursor.getColumnIndex(CANTIDAD))
            )

        }
        sqliteDatabase.close()
        return producto!!
    }

    fun getAllData(): ArrayList<Producto> {
        val listStudent = ArrayList<Producto>()
        val sqliteDatabase = this.readableDatabase
        val queryGetAll = "select * from " + TABLE_NAME
        val cursor = sqliteDatabase?.rawQuery(queryGetAll, null)
        cursor?.moveToFirst()
        while (cursor?.isAfterLast?.not() as Boolean) {
            val student = Producto(
                    codProd = cursor.getInt(cursor.getColumnIndex(COD_PROD)),
                    NombreProd = cursor.getString(cursor.getColumnIndex(NOMBRE_PROD)),
                    Descripcion = cursor.getString(cursor.getColumnIndex(DESCRIPCION)),
                    cantidad = cursor.getInt(cursor.getColumnIndex(CANTIDAD))
            )
            Log.d(TAG, "id: " + cursor.getString(cursor.getColumnIndex(COD_PROD)))
            listStudent.add(student)
            cursor.moveToNext()
        }
        cursor.close()
        return listStudent
    }

}

