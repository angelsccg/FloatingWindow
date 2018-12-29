package com.angels.world.bd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.angels.library.utils.AcLogUtil;
import com.angels.world.app.MyApplication;
import com.angels.world.mode.Note;
import com.angels.world.mode.Step;
import com.angels.world.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chencg on 2017/9/8.
 */

public class StepDBManager {
    /**表名称*/
    public static final String TABLE_NAME = "step";
    /**用户表创建sql语句*/
    public static final String CREATE_TABLE = "create table "+ TABLE_NAME +"(time varchar(20),step varchar(20))";

    /**
     * 保存当天步数
     *
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */

    public static long save(long stepL) {
//        User qUser = queryUser(user.getId());
//        if(qUser != null || qUser.getId() != null){
//            return 1;
//        }
        Step step = query(System.currentTimeMillis());
        if(step == null || step.getStep() == 0){
            return add(stepL);
        }else {
            return update(step.getStep() + stepL);
        }
    }

    public static long add(long stepL) {
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();
        String time = DateUtil.longToString(System.currentTimeMillis(),"yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put("time", time);
        values.put("step", stepL);
        long result = db.insertOrThrow(TABLE_NAME, null, values);
        db.close();
        helper.close();
        return result;
    }


    /**
     * 删除
     * */
    public static void delete(String id){
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();
        String whereClause =  "id=?";
        String[] whereArgs = new String[] {id};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
        helper.close();
    }


    /**
     * 更新
     */
    public static int update(long step){
        AcLogUtil.i("update-->" + step);
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getApplication());//province varchar(20),city varchar(20),district varchar(20)
        SQLiteDatabase db = helper.getWritableDatabase();
//        db.execSQL("update "+ NOTE_TABLE_NAME +" set content=?,update_time=? where id=?",
//                    new Object[]{note.getContent(),now});
        String time = DateUtil.longToString(System.currentTimeMillis(),"yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put("step",step);
        int result =  db.update(TABLE_NAME,values,"time=?",new String[]{time});
        db.close();
        helper.close();
        return result;
    }

    /**
     * 查询全部
     * */
    public static List<Note> queryAllStep(){
        List<Note> notes = new ArrayList<>();
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null,null, null, null, null);
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
    public static Step query(long timeL){
        if(timeL <= 0){
            return null;
        }
        String timeStr = DateUtil.longToString(timeL,"yyyy-MM-dd");
        Step step = new Step();
        DatabaseHelper helper = new DatabaseHelper(MyApplication.getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, "time=?",new String[] { timeStr }, null, null, null);
        while (cursor.moveToNext()) {
            step.setTime(cursor.getString(cursor.getColumnIndex("time")));
            step.setStep(cursor.getLong(cursor.getColumnIndex("step")));
        }
        cursor.close();
        db.close();
        helper.close();
        return step;
    }
}
