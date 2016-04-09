package com.kof.followyourgoal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
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

import android.text.format.DateFormat;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by kof on 2016/3/31.
 */
public class GoalFragment extends Fragment {

    public static final String EXTRA_GOAL_ID = "com.kof.android.followyourgoal.goal_id";
    private static final String DIALOG_DATE  = "date";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 1;

    private Goal mGoal;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mPartnerButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

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
        mPhotoFile = GoalLab.get(getActivity()).getPhotoFile(mGoal);

    }

    @Override
    public void onPause(){
        super.onPause();

        GoalLab.get(getActivity()).updateGoal(mGoal);
    }

    public void updateDate(){
        mDateButton.setText(mGoal.getDate().toString());
    }

    private String getGoalReport(){
        String solvedString = null;
        if (mGoal.isSolved()){
            solvedString = getString(R.string.goal_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mGoal.getDate()).toString();

        String partner = mGoal.getPartner();
        if (partner == null){
            partner = getString(R.string.goal_report_partner);
        }else {
            partner = getString(R.string.goal_report_partner, partner);
        }

        String report = getString(R.string.goal_report,
                mGoal.getTitle(), dateString, solvedString, partner);
        return report;
    }
    //16.9 Updating mPhotoView
    private void updatePhotoView(){
        if (mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
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

        mReportButton = (Button)v.findViewById(R.id.goal_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getGoalReport());

                getString(R.string.goal_report_subject);
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
                // TODO: 2016/4/8   ShareCompat.IntentBuilder
            }

        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //pickContact.addCategory(Intent.CATEGORY_HOME); //dummy code
        mPartnerButton = (Button)v.findViewById(R.id.goal_partner);
        mPartnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mGoal.getPartner() != null){
            mPartnerButton.setText(mGoal.getPartner());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null){
            mPartnerButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton)v.findViewById(R.id.goal_camera);



        final Intent captrueImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captrueImage.resolveActivity(packageManager) != null;
        if (canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captrueImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captrueImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView)v.findViewById(R.id.goal_photo);
        updatePhotoView();

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
        }else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            //指定你想要查询到的范围
            //values for.
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            //执行查询操作 - contactUri就像是"where"
            //clause here
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try {
                //Double-check that you actually got results
                if (c.getCount() == 0) {

                    return;
                }
                //pull out 第一行第一列的数据
                //那就是你搭档的名字
                c.moveToFirst();
                String partner = c.getString(0);
                mGoal.setPartner(partner);
                mPartnerButton.setText(partner);

            } finally {
                c.close();
            }
        }else if (requestCode == REQUEST_PHOTO){
            updatePhotoView();
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
