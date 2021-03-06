package com.kof.followyourgoal;

import java.util.Date;
import java.util.UUID;

/**
 * Created by kof on 2016/3/31.
 */
public class Goal {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;



    private String mPartner;


    public Goal(){
        this(UUID.randomUUID());
    }

    public Goal(UUID id){
        mId = id;
        mDate = new Date();
    }

    @Override
    public String toString(){
        return mTitle;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getPartner() {
        return mPartner;
    }

    public void setPartner(String partner) {
        mPartner = partner;
    }

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }


}
