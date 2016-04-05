package com.kof.followyourgoal;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by kof on 2016/3/31.
 */
public class GoalLab {
    private ArrayList<Goal> mGoals;

    private static GoalLab sGoalLab;
    private Context mAppContext;

    private GoalLab(Context appContext){
        mAppContext = appContext;
        mGoals = new ArrayList<Goal>();
        for (int i = 0; i < 100; i++) {
            Goal g = new Goal();
            g.setTitle("Crime #" + i);
            g.setSolved(i % 2 == 0); // every other one
            mGoals.add(g);
        }
    }

    public static GoalLab get(Context c){
        if (sGoalLab == null){
            sGoalLab = new GoalLab(c.getApplicationContext());
        }
        return sGoalLab;
    }

    public Goal getGoal(UUID id){
        for (Goal g : mGoals){
            if (g.getId().equals(id))
                return g;
        }
        return null;
    }

    public ArrayList<Goal> getGoals(){
        return mGoals;
    }
}
