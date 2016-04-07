package com.kof.followyourgoal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kof.followyourgoal.database.GoalDbSchema.GoalTable;

/**
 * Created by kof on 2016/4/7.
 */
public class GoalBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "goalBase.db";

    public GoalBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + GoalTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                GoalTable.Cols.UUID + ", " +
                GoalTable.Cols.TITLE + ", " +
                GoalTable.Cols.DATE + ", " +
                GoalTable.Cols.SOLVED + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}

