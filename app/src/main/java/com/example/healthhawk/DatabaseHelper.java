/*
Name:   DatabaseHelper
Author: Sebastian Koller
Version: 3.0 (12/3/2021)
Status: COMPLETE (COULD BE CHANGES)
 */
package com.example.healthhawk;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    /* ------------ Class Members ------------ */

    private static final String DATABASE_NAME = "HealthHawk Database";
    private static final int DATABASE_VERSION = 1;

    /* ------------ Users Table ------------ */
    public static final String USERS_TABLE_NAME       = "users";
    public static final String USERS_COLUMN_EMAIL     = "email";
    public static final String USERS_COLUMN_PASSWORD  = "password";
    public static final String USERS_COLUMN_NAME      = "name";

    private static final String CREATE_USERS = "CREATE TABLE " +USERS_TABLE_NAME+"("
            +USERS_COLUMN_EMAIL       + " Text primary key, "
            +USERS_COLUMN_PASSWORD    + " Text, "
            +USERS_COLUMN_NAME        + " Text)";

    /* ------------ Foods Table ------------ */
    public static final String FOODS_TABLE_NAME           = "foods";
    public static final String FOODS_COLUMN_FOOD_ID       = "food_id";
    public static final String FOODS_COLUMN_USER          = "user";
    public static final String FOODS_COLUMN_FOOD_NAME     = "food_name";
    public static final String FOODS_COLUMN_CALORIES      = "calories";
    public static final String FOODS_COLUMN_FOOD_TYPE_ID  = "food_type_id";
    public static final String FOODS_COLUMN_MEAL_TYPE_ID  = "meal_type_id";

    private static final String CREATE_FOODS = "CREATE TABLE " +FOODS_TABLE_NAME+"("
            +FOODS_COLUMN_FOOD_ID      + " integer primary key autoincrement, "
            +FOODS_COLUMN_USER         + " Text, "
            +FOODS_COLUMN_FOOD_NAME    + " Text, "
            +FOODS_COLUMN_CALORIES     + " integer, "
            +FOODS_COLUMN_FOOD_TYPE_ID + " integer, "
            +FOODS_COLUMN_MEAL_TYPE_ID + " integer)";

    /* ------------ Dates Table ------------ */
    public static final String DATES_TABLE_NAME           = "dates";
    public static final String DATES_COLUMN_DATE_ID       = "date_id";
    public static final String DATES_COLUMN_USER          = "user";
    public static final String DATES_COLUMN_DATE_NAME     = "date_name";
    public static final String DATES_COLUMN_START_DATE    = "start_date";
    public static final String DATES_COLUMN_END_DATE      = "end_date";
    public static final String DATES_COLUMN_DESCRIPTION   = "description";
    public static final String DATES_COLUMN_REMINDER  	  = "reminder";

    private static final String CREATE_DATES = "CREATE TABLE " +DATES_TABLE_NAME+"("
            +DATES_COLUMN_DATE_ID      + " integer primary key autoincrement, "
            +DATES_COLUMN_USER         + " Text, "
            +DATES_COLUMN_DATE_NAME    + " Text, "
            +DATES_COLUMN_START_DATE   + " integer, "
            +DATES_COLUMN_END_DATE 	   + " integer, "
            +DATES_COLUMN_DESCRIPTION  + " Text,"
            +DATES_COLUMN_REMINDER     + " boolean)";

    /* ------------ Workouts Table ------------ */
    public static final String WORKOUTS_TABLE_NAME             = "workouts";
    public static final String WORKOUTS_COLUMN_WORKOUT_NAME    = "workout_name";
    public static final String WORKOUTS_COLUMN_SETS 		   = "sets";
    public static final String WORKOUTS_COLUMN_EXERCISE_LIST   = "exercise_list";
    public static final String WORKOUTS_COLUMN_EXERCISE_TIME   = "time_per_exercise";
    public static final String WORKOUTS_COLUMN_REST_TIME       = "rest";
    public static final String WORKOUTS_COLUMN_RECOVERY_TIME   = "recovery";




    private static final String CREATE_WORKOUTS = "CREATE TABLE " +WORKOUTS_TABLE_NAME+"("
            +WORKOUTS_COLUMN_WORKOUT_NAME    + " String primary key, "
            +WORKOUTS_COLUMN_SETS   		 + " integer, "
            +WORKOUTS_COLUMN_EXERCISE_LIST   + " Text, "
            +WORKOUTS_COLUMN_EXERCISE_TIME 	 + " Decimal, "
            +WORKOUTS_COLUMN_REST_TIME  	 + " Decimal,"
            +WORKOUTS_COLUMN_RECOVERY_TIME   + " Decimal)";

    /* ------------ Goals Table ------------ */
    public static final String GOALS_TABLE_NAME           = "goals";
    public static final String GOALS_COLUMN_DATE_ID       = "date_id";
    public static final String GOALS_COLUMN_USER          = "user";
    public static final String GOALS_COLUMN_GOAL_NAME     = "goal_name";
    public static final String GOALS_COLUMN_START_DATE    = "start_date";
    public static final String GOALS_COLUMN_END_DATE      = "end_date";
    public static final String GOALS_COLUMN_DESCRIPTION   = "description";
    public static final String GOALS_COLUMN_ACHIEVED  	  = "achieved";

    private static final String CREATE_GOALS = "CREATE TABLE " +GOALS_TABLE_NAME+"("
            +GOALS_COLUMN_DATE_ID      + " integer primary key autoincrement, "
            +GOALS_COLUMN_USER         + " Text, "
            +GOALS_COLUMN_GOAL_NAME    + " Text, "
            +GOALS_COLUMN_START_DATE   + " integer, "
            +GOALS_COLUMN_END_DATE 	   + " integer, "
            +GOALS_COLUMN_DESCRIPTION  + " Text,"
            +GOALS_COLUMN_ACHIEVED     + " boolean)";

    /* ------------ Calendar Table  ------------ */

    public static final String CALENDAR_Date= "Date";
    public static final String CALENDAR_Event="Event";
    public static final String CALENDAR_TABLE_NAME           = "EventCalendar";

    private static final String CALENDAR = "CREATE TABLE " +CALENDAR_TABLE_NAME+"("
            +FOODS_COLUMN_FOOD_ID      + " integer primary key autoincrement, "
            +CALENDAR_Date         + " Text, "
            +CALENDAR_Event    + " Text )";


    /* ------------ Constructor ------------ */
    public DatabaseHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    /* ------------ Lifecycle Methods ------------ */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_FOODS);
        db.execSQL(CREATE_DATES);
        db.execSQL(CREATE_GOALS);
        db.execSQL(CREATE_WORKOUTS);
        db.execSQL(CALENDAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FOODS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GOALS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DATES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WORKOUTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +CALENDAR_TABLE_NAME);
    }
}