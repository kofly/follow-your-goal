package com.kof.followyourgoal;

import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by kof on 2016/4/5.
 */
public class GoalActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID goalId = (UUID)getIntent()
                .getSerializableExtra(GoalFragment.EXTRA_GOAL_ID);
        return GoalFragment.newInstance(goalId);
    }
}