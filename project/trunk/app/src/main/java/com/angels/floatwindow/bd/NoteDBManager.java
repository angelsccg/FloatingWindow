package com.angels.floatwindow.bd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.angels.floatwindow.app.MyApplication;
import com.angels.floatwindow.mode.Note;
import com.angels.library.utils.AcLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chencg on 2017/9/8.
 */

public class NoteDBManager {
    /**表名称*/
    public static final String NOTE_TABLE_NAME = "note";
    /**用户表创建sql语句*/
    public static final String CREATE_NOTE_TABLE = "create table "+ NOTE_TABLE_NAME +"(id INTEGER PRIMARY KEY AUTOINCREMENT,content text,create_time varchar(20),update_time varchar(20))";

    /**
     * 添加用户 (如果本地数据库已经保存了该用户，就修改用户信息)
     *
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */

    public static long addNote(Note note) {
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();
//        User qUser = queryUser(user.getId());
//        if(qUser != null || qUser.getId() != null){
//            return 1;
//        }
        long create_time = System.currentTimeMillis();
        long update_time = System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put("content", note.getContent());
        values.put("create_time", create_time);
        values.put("update_time", update_time);
        long result = db.insertOrThrow(NOTE_TABLE_NAME, null, values);
        db.close();
        helper.close();
        return result;
    }

    /**
     * 删除user
     * */
    public static void deleteNote(String id){
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();
        String whereClause =  "id=?";
        String[] whereArgs = new String[] {id};
        db.delete(NOTE_TABLE_NAME, whereClause, whereArgs);
        db.close();
        helper.close();
    }

    /**
     * 删除全部user
     * */
    public static void deletAllNote(){
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM "+ NOTE_TABLE_NAME);
        db.close();
        helper.close();
    }


    /**
     * 修改
     */
    public static void updateNote(Note note){
        AcLogUtil.i("updateNote-->" + note.getContent() + "--" + note.getId());
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getApplication());//province varchar(20),city varchar(20),district varchar(20)
        SQLiteDatabase db = helper.getWritableDatabase();
        long now = System.currentTimeMillis();
//        db.execSQL("update "+ NOTE_TABLE_NAME +" set content=?,update_time=? where id=?",
//                    new Object[]{note.getContent(),now});
        ContentValues values = new ContentValues();
        values.put("content",note.getContent());
        values.put("update_time",now);
        db.update(NOTE_TABLE_NAME,values,"id=?",new String[]{note.getId()});
        db.close();
        helper.close();
    }

    /**
     * 查询全部user
     * */
    public static List<Note> queryAllNote(){
        List<Note> notes = new ArrayList<>();
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(NOTE_TABLE_NAME, null, null,null, null, null, null);
        while (cursor.moveToNext()) {
            Note note = new Note();
            int id = cursor.getColumnIndex("id");
            int content = cursor.getColumnIndex("content");
            int create_time = cursor.getColumnIndex("create_time");
            int update_time = cursor.getColumnIndex("update_time");
            note.setId(cursor.getString(id));
            note.setContent(cursor.getString(content));
            note.setCreateTime(cursor.getLong(create_time));
            note.setUpdateTime(cursor.getLong(update_time));
            notes.add(note);
        }
        cursor.close();
        db.close();
        helper.close();
        return notes;
    }

    /**
     * 查询最新一条
     * */
    public static Note queryNewNote(){
        Note note = new Note();
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(NOTE_TABLE_NAME, null, null,null, null, null, null);
        if (cursor.moveToLast()) {
            int id = cursor.getColumnIndex("id");
            int content = cursor.getColumnIndex("content");
            int create_time = cursor.getColumnIndex("create_time");
            int update_time = cursor.getColumnIndex("update_time");
            note.setId(cursor.getString(id));
            note.setContent(cursor.getString(content));
            note.setCreateTime(cursor.getLong(create_time));
            note.setUpdateTime(cursor.getLong(update_time));
        }
        cursor.close();
        db.close();
        helper.close();
        return note;
    }
}
