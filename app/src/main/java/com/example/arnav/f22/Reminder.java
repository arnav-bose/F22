package com.example.arnav.f22;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Reminder extends Fragment {

    ListView listViewReminder;
    ArrayList<String> arrayListReminders;
    public static ArrayAdapter<String> arrayAdapterReminders;


    public Reminder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        listViewReminder = (ListView)view.findViewById(R.id.list_view_reminders);
        arrayListReminders = Utils.getArrayList(getContext());
        arrayAdapterReminders = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayListReminders);
        listViewReminder.setAdapter(arrayAdapterReminders);
        setHasOptionsMenu(true);

        listViewReminder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder reminderDialog = new AlertDialog.Builder(getActivity());
                String[] types = {"Delete Reminder"};
                reminderDialog.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0: {
                                arrayListReminders.remove(i);
                                arrayAdapterReminders.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                });
                reminderDialog.show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_refresh:
                arrayAdapterReminders.clear();
                arrayListReminders = Utils.getArrayList(getContext());
                arrayAdapterReminders = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayListReminders);
                listViewReminder.setAdapter(arrayAdapterReminders);
        }

        return super.onOptionsItemSelected(item);
    }
}
