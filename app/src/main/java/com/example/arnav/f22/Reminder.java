package com.example.arnav.f22;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Reminder extends Fragment {

    ListView listViewReminder;


    public Reminder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        listViewReminder = (ListView)view.findViewById(R.id.list_view_reminders);
        ArrayList<String> arrayListReminders = Utils.getArrayList(getContext());
        ArrayAdapter<String> arrayAdapterReminders = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayListReminders);
        listViewReminder.setAdapter(arrayAdapterReminders);

        // Inflate the layout for this fragment
        return view;
    }

}
