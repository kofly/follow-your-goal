package com.kof.followyourgoal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by kof on 2016/4/1.
 */
public class GoalPagerActivity extends FragmentActivity {
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        final ArrayList<Goal> mGoals = GoalLab.get(this).getGoals();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Goal goal = mGoals.get(position);
                return GoalFragment.newInstance(goal.getId());
            }

            @Override
            public int getCount() {
                return mGoals.size();
            }
        });

;
        UUID goalId = (UUID)getIntent().getSerializableExtra(GoalFragment.EXTRA_GOAL_ID);
        for (int i = 0; i<mGoals.size(); i++){
            if (mGoals.get(i).getId().equals(goalId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
