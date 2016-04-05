package com.kof.followyourgoal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kof on 2016/3/31.
 */
public class GoalListFragment extends ListFragment {
    private ArrayList<Goal> mGoals;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.goals_title);
        mGoals = GoalLab.get(getActivity()).getGoals();
        GoalAdapter adapter = new GoalAdapter(mGoals);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int positon, long id){
        Goal g =( (GoalAdapter)getListAdapter()).getItem(positon);
        Intent i = new Intent(getActivity(), GoalPagerActivity.class);
        i.putExtra(GoalFragment.EXTRA_GOAL_ID, g.getId());
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((GoalAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class GoalAdapter extends ArrayAdapter<Goal>{

        public GoalAdapter(ArrayList<Goal> Goals){
            super(getActivity(), android.R.layout.simple_list_item_1, Goals);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_goal, null);
            }

            Goal g = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.goal_list_item_titleTextView);
            titleTextView.setText(g.getTitle());
            TextView dateTextView = (TextView)convertView.findViewById(R.id.goal_list_item_dateTextView);
            dateTextView.setText(g.getDate().toString());
            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.goal_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(g.isSolved());

            return  convertView;
        }
    }
}
