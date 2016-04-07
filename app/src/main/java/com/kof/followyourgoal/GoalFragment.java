package com.kof.followyourgoal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by kof on 2016/3/31.
 */
public class GoalFragment extends Fragment {

    public static final String EXTRA_GOAL_ID = "com.kof.android.followyourgoal.goal_id";
    private static final String DIALOG_DATE  = "date";
    private static final int REQUEST_DATE = 0;

    private Goal mGoal;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    public static GoalFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_GOAL_ID, crimeId);

        GoalFragment fragment = new GoalFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID goalId = (UUID)getArguments().getSerializable(EXTRA_GOAL_ID);
        mGoal = GoalLab.get(getActivity()).getGoal(goalId);

    }

    @Override
    public void onPause(){
        super.onPause();

        GoalLab.get(getActivity()).updateGoal(mGoal);
    }

    public void updateDate(){
        mDateButton.setText(mGoal.getDate().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_goal, parent ,false);

        mTitleField = (EditText)v.findViewById(R.id.goal_title);
        mTitleField.setText(mGoal.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mGoal.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDateButton = (Button)v.findViewById(R.id.goal_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mGoal.getDate());
                dialog.setTargetFragment(GoalFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.goal_solved);
        mSolvedCheckBox.setChecked(mGoal.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mGoal.setSolved(isChecked);
            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE){
            Date date = (Date)data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mGoal.setDate(date);
            updateDate();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_goal, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_goal:
                Intent intent = new Intent(getActivity(), GoalListActivity.class);
                startActivity(intent);
                GoalLab.get(getActivity()).deleteGoal(mGoal);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
