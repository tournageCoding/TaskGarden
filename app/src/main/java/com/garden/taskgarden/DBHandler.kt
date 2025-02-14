package com.garden.taskgarden

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*

/**
 *
 * This class is directly responsible for interacting with and maintaining the database.
 * Contains methods to create, update, delete, and get handlers in the database.
 *
 * DO NOT INTERFACE WITH THIS CLASS DIRECTLY!!!
 * IF YOU WANT TO TALK TO THE DATABASE, PLEASE TALK TO DBInterface
 *
 * @author Blake MacDade
 */
class DBHandler(context: Context?, name: String?, factory: CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        var CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
        CREATE_TABLE += COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_COMPLETED + " INTEGER, " +
                COLUMN_COMPLETED_BY + " TEXT " +
                ")"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Do we need this func?
    }

    /**
     * loadUncompletedHandler gets all tasks from the database that have their completed flag set to 0,
     * or not completed.
     *
     * @return an ArrayList of uncompleted Task objects
     * */
    fun loadUncompletedHandler(): ArrayList<Task> {
        val taskArray = ArrayList<Task>()
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            if (cursor.getInt(3) == 0) {
                taskArray.add(Task(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4))
                )
            }
        }
        cursor.close()
        db.close()
        return taskArray
    }

    /**
     * loadCompletedHandlers gets all completed tasks from the database
     *
     * @return an ArrayList of completed Task objects.
     * */
    fun loadCompletedHandlers(): ArrayList<Task> {
        val taskArray = ArrayList<Task>()
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            if (cursor.getInt(3) == 1 || cursor.getInt(3) == 2) {
                taskArray.add(Task(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4))
                )
            }
        }
        cursor.close()
        db.close()
        return taskArray
    }

    /**
     * addHandler is a dumb method, attempts to add a task to the database at the index of task.taskID.
     *
     * @param task the task object to add to the database.
     */
    fun addHandler(task: Task) {
        try {
            val values = ContentValues()
            values.put(COLUMN_ID, task.iD)
            values.put(COLUMN_NAME, task.title)
            values.put(COLUMN_COMPLETED, task.completed)
            values.put(COLUMN_COMPLETED_BY, task.timeToCompletedBy)
            values.put(COLUMN_DESCRIPTION, task.description)
            val db = this.writableDatabase
            db.insert(TABLE_NAME, null, values)
            db.close()
        } catch (e: SQLiteConstraintException) {
            Log.d(debugTag, "Got SQLiteConstraintException in addHandler method of DBHandler")
        }
    }

    /**
     * findHandler returns a specific task from the database.
     *
     * @param taskId the ID of the task to get from the database.
     * @return Task object corresponding to the entry in the database.
     * */
    fun findHandler(taskId: Int): Task {
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = '$taskId'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        val task = Task()
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            task.setID(cursor.getInt(0))
            task.setTitle(cursor.getString(1))
            task.setDescription(cursor.getString(2))
            when (cursor.getInt(3)) {
                1 -> task.setCompleted(1)
                0 -> task.setCompleted(0)
                2 -> task.setCompleted(2)
                else -> task.setCompleted(0)
            }
            task.setCompletedBy(cursor.getString(4))
        }
        cursor.close()
        db.close()
        return task
    }
    /**
     * deleteHandler removes an entry from the database.
     *
     * @param ID the primary key of the entry to delete from the database.
     * @return status of the deletion.
     * */
    fun deleteHandler(ID: Int): Boolean {
        var result = false
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = '$ID'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        val task = Task()
        if (cursor.moveToFirst()) {
            task.setID(cursor.getInt(0))
            db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(java.lang.String.valueOf(task.iD)))
            // for the love of GOD, please revisit this steaming pile of crap, took from online tutorial and just want it to work :)
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    /**
     * updateTask updates a task record in the database
     *
     * @param task the updated task to pass to the database.
     *
     * @return the number of rows updated.
     */
    fun updateTask(task: Task?): Boolean {
        val db = this.writableDatabase
        val args = ContentValues()
        //args.put(COLUMN_ID,ID);
        if(task!= null) {
            args.put(COLUMN_NAME, task.title)
            args.put(COLUMN_COMPLETED, task.completed)
            args.put(COLUMN_COMPLETED_BY, task.timeToCompletedBy)
            args.put(COLUMN_DESCRIPTION, task.description)
            return db.update(TABLE_NAME, args, COLUMN_ID + " = " + task.iD, null) > 0
        }
        return false
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "taskDB.db"
        const val TABLE_NAME = "Tasks"
        const val COLUMN_ID = "TaskID"
        const val COLUMN_NAME = "TaskTitle"
        const val COLUMN_DESCRIPTION = "TaskDescription"
        const val COLUMN_COMPLETED = "Completed"
        const val COLUMN_COMPLETED_BY = "CompletedBy"
        const val debugTag = "DBHandler"
    }
}
