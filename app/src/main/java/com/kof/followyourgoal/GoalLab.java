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

    public void addGoal(Goal g){
        mGoals.add(g);
    }

    public void deleteGoal(Goal g){
        mGoals.remove(g);
    }
}
