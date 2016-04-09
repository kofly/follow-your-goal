package com.kof.followyourgoal.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.kof.followyourgoal.Goal;
import com.kof.followyourgoal.database.GoalDbSchema.GoalTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by kof on 2016/4/7.
 */
public class GoalCursorWrapper extends CursorWrapper{
    public GoalCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Goal getGoal(){
        String uuidString = getString(getColumnIndex(GoalTable.Cols.UUID));
        String title = getString(getColumnIndex(GoalTable.Cols.TITLE));
        long date = getLong(getColumnIndex(GoalTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(GoalTable.Cols.SOLVED));
        String partner = getString(getColumnIndex(GoalTable.Cols.PARTNER));

        Goal goal = new Goal(UUID.fromString(uuidString));
        goal.setTitle(title);
        goal.setDate(new Date(date));
        goal.setSolved(isSolved != 0);
        goal.setPartner(partner);

        return goal;
    }
}
