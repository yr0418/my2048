package com.example.my2048.tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    //数据库版本
    private static final int DB_VERSION = 1;
    //数据库名字
    private static final String DB_NAME = "Onwaiers.db";
    //表名
    public static final String TABLE_NAME = "s";
    private Context context;
    //构造方法建立数据库
    public MyDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    //自动调用该方法，当数据库中不存在该表的时候自动创建该表
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table s (Id int primary key, Score integer)";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS s";
        db.execSQL(sql);
        onCreate(db);
    }
    public String getDbName(){
        return DB_NAME;
    }

    public String getTableName(){
        return TABLE_NAME;
    }
}
