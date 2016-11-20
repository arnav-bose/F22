package com.example.arnav.f22;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Arnav on 13/11/2016.
 */
public class Utils {

    public static ArrayList<String> arrayListReminders = new ArrayList<>();
    public static String mySharedPreferences = "sharedPreferences";
    public static SharedPreferences sharedPreferences;

    public static ArrayList<String> getArrayList(Context context){
        sharedPreferences = context.getSharedPreferences(Utils.mySharedPreferences, Context.MODE_PRIVATE);
        String reminder = sharedPreferences.getString("reminders", "");
        String[] reminderNames = reminder.split(",");
        if(reminderNames.length == 1){
            arrayListReminders.add("");
        }
        else{
            for(int i = 1; i< reminderNames.length; i++){
                arrayListReminders.add(reminderNames[i]);
            }
        }

        return arrayListReminders;
    }

}
