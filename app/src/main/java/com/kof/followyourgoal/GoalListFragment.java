package com.kof.followyourgoal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

public class GoalListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mGoalRecyclerView;
    private GoalAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_list, container, false);

        mGoalRecyclerView = (RecyclerView) view
                .findViewById(R.id.goal_recycler_view);
        mGoalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_goal_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_goal:
                Goal goal = new Goal();
                GoalLab.get(getActivity()).addGoal(goal);
                Intent intent = GoalPagerActivity
                        .newIntent(getActivity(), goal.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        GoalLab goalLab = GoalLab.get(getActivity());
        int goalCount = goalLab.getGoals().size();
        String subtitle = getString(R.string.subtitle_format, goalCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        GoalLab goalLab = GoalLab.get(getActivity());
        List<Goal> goals = goalLab.getGoals();

        if (mAdapter == null) {
            mAdapter = new GoalAdapter(goals);
            mGoalRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class GoalHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        private Goal mGoal;

        public GoalHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.goal_list_item_titleTextView);
            mDateTextView = (TextView) itemView.findViewById(R.id.goal_list_item_dateTextView);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.goal_list_item_solvedCheckBox);
        }

        public void bindGoal(Goal goal) {
            mGoal = goal;
            mTitleTextView.setText(mGoal.getTitle());
            mDateTextView.setText(mGoal.getDate().toString());
            mSolvedCheckBox.setChecked(mGoal.isSolved());
        }

        @Override
        public void onClick(View v) {
            Intent intent = GoalPagerActivity.newIntent(getActivity(), mGoal.getId());
            startActivity(intent);
        }
    }

    private class GoalAdapter extends RecyclerView.Adapter<GoalHolder> {

        private List<Goal> mGoals;

        public GoalAdapter(List<Goal> goals) {
            mGoals = goals;
        }

        @Override
        public GoalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_goal, parent, false);
            return new GoalHolder(view);
        }

        @Override
        public void onBindViewHolder(GoalHolder holder, int position) {
            Goal goal = mGoals.get(position);
            holder.bindGoal(goal);
        }

        @Override
        public int getItemCount() {
            return mGoals.size();
        }
    }
}

