package com.kof.followyourgoal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kof.followyourgoal.database.GoalBaseHelper;
import com.kof.followyourgoal.database.GoalCursorWrapper;

import com.kof.followyourgoal.database.GoalDbSchema.GoalTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by kof on 2016/3/31.
 */
public class GoalLab {

    private static GoalLab sGoalLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private GoalLab(Context context){
        mContext = context.getApplicationContext();//为什么将上下文存于实例变量中？
        mDatabase = new GoalBaseHelper(mContext).getWritableDatabase();
    }

    public static GoalLab get(Context c){
        if (sGoalLab == null){
            sGoalLab = new GoalLab(c.getApplicationContext());
        }
        return sGoalLab;
    }

    public Goal getGoal(UUID id){
        GoalCursorWrapper cursor = (GoalCursorWrapper) queryGoals(GoalTable.Cols.UUID + "=?", new String[]{id.toString()});

        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getGoal();

        }finally {
            cursor.close();
        }
    }

    public void updateGoal(Goal goal){
        String uuidString = goal.getId().toString();
        ContentValues values = getContentValues(goal);

        mDatabase.update(GoalTable.NAME, values, GoalTable.Cols.UUID + "= ?", new String[]{uuidString});
    }

    private static ContentValues getContentValues(Goal goal){
        ContentValues values = new ContentValues();
        values.put(GoalTable.Cols.UUID, goal.getId().toString());
        values.put(GoalTable.Cols.TITLE, goal.getTitle());
        values.put(GoalTable.Cols.DATE, goal.getDate().getTime());
        values.put(GoalTable.Cols.SOLVED, goal.isSolved() ? 1 : 0);

        return values;

    }

    private Cursor queryGoals(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                GoalTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GoalCursorWrapper(cursor);
    }

    public List<Goal> getGoals() {
        List<Goal> goals = new ArrayList<>();

        GoalCursorWrapper cursor = (GoalCursorWrapper) queryGoals(null, null);//cast

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            goals.add(cursor.getGoal());
            cursor.moveToNext();
        }
        cursor.close();

        return goals;
    }

    public void addGoal(Goal g){
        ContentValues values = getContentValues(g);
        mDatabase.insert(GoalTable.NAME, null, values);
    }

   public void deleteGoal(Goal g){

        mDatabase.delete(GoalTable.NAME, GoalTable.Cols.TITLE +"=?", new String[]{g.getTitle()});//// TODO: 2016/4/7  数据库删除
    }
}
