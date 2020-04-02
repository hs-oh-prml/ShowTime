package com.showtime.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.showtime.R
import com.showtime.data.LectureItem
import com.showtime.data.MajorItem
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList

class DBHelper (var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val DB_NAME = "lecture.db"
    override fun onCreate(db: SQLiteDatabase) {
//
//        var dbFile = context.getDatabasePath(DB_NAME)
//
//        if(!dbFile.exists()){
//            try{
//                var checkDB= context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE,null)
//                checkDB?.close()
//                copyDatabase(dbFile)
//            } catch (e: IOException){
//                throw RuntimeException("Error creating source database", e)
//            }
//        }
//        SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)

//        db.execSQL(SQL_CREATE_ENTRIES)
    }

    fun checkDB(): Boolean{
        var checkDB:SQLiteDatabase? = null
        try{
            var checkDB= context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE,null)
            checkDB?.close()
//            copyDatabase(dbFile)
        } catch (e: IOException){
                throw RuntimeException("Error creating source database", e)
        }
        return checkDB != null
    }
    @SuppressLint("WrongConstant")
    fun copyDatabase(){
        var dbFile = context.getDatabasePath(DB_NAME)
        val inputStream = context.resources.openRawResource(R.raw.lecture)
//        val inputStream = context.assets.open(DB_NAME)
        var outputStream = FileOutputStream(dbFile)
        val buffer = ByteArray(1024)
        while(inputStream.read(buffer) > 0){
            outputStream.write(buffer)
            Log.d("#DB", "Writing>>")
        }
        outputStream.flush()
        outputStream.close()
        inputStream.close()
        Log.d("#DB", "Completed..")
    }
    @Throws(IOException::class)
    fun importDB() {
        val ex = checkDB()
        if (!ex) {
            this.readableDatabase
            try {
                copyDatabase()
            } catch (e: IOException) { }
        }
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun getList(departCode:String?, s_pobtDiv:String):ArrayList<LectureItem>{
        val lectArrayList = ArrayList<LectureItem>()
        val db = readableDatabase

        var query = ""
        var arg:Array<String>
        val cursor: Cursor

        if(departCode != null){
            if(s_pobtDiv == "전체"){
                arg = arrayOf("%${departCode}%")
                query = "SELECT * FROM ${Lecture.TABLE_NAME} WHERE majorCode LIKE ?"
            } else {
                arg = arrayOf("%${departCode}%", "%${s_pobtDiv}%")
                query = "SELECT * FROM ${Lecture.TABLE_NAME} WHERE majorCode LIKE ? and pobtDiv LIKE ?"
            }
            cursor = db.rawQuery(query, arg)

        } else {
            if(s_pobtDiv == "전체"){
                query = "SELECT * FROM ${Lecture.TABLE_NAME}"
                cursor = db.rawQuery(query, null)

            } else {
                arg = arrayOf("%${s_pobtDiv}%")
                query = "SELECT * FROM ${Lecture.TABLE_NAME} WHERE pobtDiv LIKE ?"
                cursor = db.rawQuery(query, arg)

            }
        }

        Log.d("SQL", query)

        Log.d("SQL RESULT", cursor.count.toString())

        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    val grade = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_GRADE))
                    var classCode = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_CLASSCODE))
                    var pobtDiv = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_POBTDIV))
                    var lectNum = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_LECTNUM))
                    var lectName = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_LECTNAME))
                    var credit = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_CREDIT))
                    var lectHour = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_LECTHOUR))
                    var major = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_MAJOR))
                    var timeNroom = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_TIMENROOM))
                    var professor = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_PROFESSOR))
                    var data = LectureItem(grade, classCode, pobtDiv, lectNum, lectName,credit, lectHour, major, timeNroom, professor)
                    lectArrayList.add(data)
                } while (cursor.moveToNext())
            }
        }
        Log.d("Search List", lectArrayList.toString())
        return lectArrayList
    }

    fun search(keyword:String, mode:String, majorCode:String, pobtDiv:String):ArrayList<LectureItem>{
        val lectArrayList = ArrayList<LectureItem>()
        val db = readableDatabase

        var arg:Array<String>
        var query = ""
        if(majorCode == ""){
            if(pobtDiv == "전체"){
                arg = arrayOf("%${keyword}%")
                query = "SELECT * FROM ${Lecture.TABLE_NAME} WHERE ${mode} LIKE ?"
            } else {
                arg = arrayOf("%${keyword}%", "%${pobtDiv}")
                query = "SELECT * FROM ${Lecture.TABLE_NAME} WHERE ${mode} LIKE ? AND pobtDiv LIKE ?"
            }
        } else {
            if(pobtDiv == "전체"){
                arg = arrayOf("%${keyword}%", "%${majorCode}%")
                query = "SELECT * FROM ${Lecture.TABLE_NAME} WHERE ${mode} LIKE ? AND majorCode LIKE ?"
            } else {
                arg = arrayOf("%${keyword}%", "%${majorCode}%", "%${pobtDiv}")
                query = "SELECT * FROM ${Lecture.TABLE_NAME} WHERE ${mode} LIKE ? AND majorCode LIKE ? AND pobtDiv LIKE ?"
            }
        }
        Log.d("SQL", query)
        val cursor = db.rawQuery(query, arg)
        Log.d("SQL RESULT", cursor.count.toString())

        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    val grade = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_GRADE))
                    var classCode = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_CLASSCODE))
                    var pobtDiv = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_POBTDIV))
                    var lectNum = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_LECTNUM))
                    var lectName = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_LECTNAME))
                    var credit = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_CREDIT))
                    var lectHour = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_LECTHOUR))
                    var major = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_MAJOR))
                    var timeNroom = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_TIMENROOM))

                    var professor = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_PROFESSOR))
                    var data = LectureItem(grade, classCode, pobtDiv, lectNum, lectName,credit, lectHour, major, timeNroom, professor)
                    lectArrayList.add(data)
                } while (cursor.moveToNext())
            }
        }

        return lectArrayList
    }

    fun getMajor(): ArrayList<ArrayList<MajorItem>> {
        var result = ArrayList<ArrayList<MajorItem>>()

        var majorList = ArrayList<MajorItem>()
        val db = readableDatabase

        var departList = ArrayList<String>()
        var depart_query = "SELECT DISTINCT department FROM department"
        Log.d("SQL", depart_query)
        val d_cursor = db.rawQuery(depart_query, null)
        if(d_cursor != null){
            if(d_cursor.moveToFirst()){
                do {
                    val depart = d_cursor.getString(d_cursor.getColumnIndex("department"))
                    departList.add(depart)
                } while (d_cursor.moveToNext())
            }
        }

        for(i in departList){
            var row = ArrayList<MajorItem>()

//            row.add(MajorItem(i, null, null, 0))

            var row_query = "SELECT * FROM department WHERE department = ?"
            var arg = arrayOf(i)
            var m_cursor = db.rawQuery(row_query, arg)
            if(m_cursor != null){
                if(m_cursor.moveToFirst()){
                    do {
                        var depart = i
                        var major = m_cursor.getString(m_cursor.getColumnIndex("major"))
                        var code = m_cursor.getString(m_cursor.getColumnIndex("code"))
                        var data = MajorItem(depart, major, code, 1)
                        row.add(data)
                    } while(m_cursor.moveToNext())
                }
                if(row.size == 1){
                    row[0].type = 1
                } else {
                    row[0].type = 0
                }

                result.add(row)
            }
        }
        return result
    }
    fun searchLect(selection:String, selectionArgs:Array<String>): ArrayList<LectureItem> {
        val lectArrayList = ArrayList<LectureItem>()
        val db = readableDatabase

        val cursor = db.query(Lecture.TABLE_NAME,
            null, selection, selectionArgs, null, null, null)

        while (cursor.moveToNext()) {
            val grade = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_GRADE))
            var classCode = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_CLASSCODE))
            var pobtDiv = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_POBTDIV))
            var lectNum = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_LECTNUM))
            var lectName = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_LECTNAME))
            var credit = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_CREDIT))
            var lectHour = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_LECTHOUR))
            var major = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_MAJOR))
            var timeNroom = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_TIMENROOM))
            var professor = cursor.getString(cursor.getColumnIndex(Lecture.COLUMN_NAME_PROFESSOR))
            var data = LectureItem(grade, classCode, pobtDiv, lectNum, lectName,credit, lectHour, major, timeNroom, professor)
            lectArrayList.add(data)
        }
        return lectArrayList
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "lecture.db"

        val SQL_CREATE_ENTRIES = "CREATE TABLE " + Lecture.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY," +
                Lecture.COLUMN_NAME_GRADE + "  TEXT," +
                Lecture.COLUMN_NAME_CLASSCODE +
                Lecture.COLUMN_NAME_GRADE + "  TEXT," +
                Lecture.COLUMN_NAME_POBTDIV + "  TEXT," +
                Lecture.COLUMN_NAME_LECTNUM + "  TEXT," +
                Lecture.COLUMN_NAME_LECTNAME + "  TEXT," +
                Lecture.COLUMN_NAME_CREDIT + "  TEXT," +
                Lecture.COLUMN_NAME_LECTHOUR + "  TEXT," +
                Lecture.COLUMN_NAME_MAJOR + "  TEXT," +
                Lecture.COLUMN_NAME_TIMENROOM + "  TEXT," +
                Lecture.COLUMN_NAME_PROFESSOR + " TEXT )"
    }
    class Lecture : BaseColumns {
        companion object {
            val TABLE_NAME = "lecture"

            val MAJOR_TABLE_NAME = "department"

            val COLUMN_NAME_GRADE= "grade"
            val COLUMN_NAME_CLASSCODE = "classCode"
            val COLUMN_NAME_POBTDIV = "pobtDiv"
            val COLUMN_NAME_LECTNUM = "lectNum"
            val COLUMN_NAME_LECTNAME = "lectName"
            val COLUMN_NAME_CREDIT = "credit"
            val COLUMN_NAME_LECTHOUR = "lectHour"
            val COLUMN_NAME_MAJOR = "major"
            val COLUMN_NAME_TIMENROOM = "timeNroom"
            val COLUMN_NAME_PROFESSOR = "professor"
        }
    }
}