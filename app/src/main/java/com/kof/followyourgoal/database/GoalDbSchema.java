package com.kof.followyourgoal.database;

/**
 * Created by kof on 2016/4/7.
 */
public class GoalDbSchema {
    public static final class GoalTable{
        public static final String NAME = "goals";


    public static final class Cols{
        public static final String UUID = "uuid";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String SOLVED = "solved";
        public static final String PARTNER = "partner";
    }
    }
}
