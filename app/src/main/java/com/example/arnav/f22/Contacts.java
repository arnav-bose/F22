package com.example.arnav.f22;


import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Contacts extends Fragment {

    ListView listViewContacts;
    ArrayList<String> arrayListContacts = new ArrayList<>();
    private static final int REQUEST_CODE = 1;
    private final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    private final String CALL_PHONE = Manifest.permission.CALL_PHONE;
    private final String READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
    private final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    private DatePickerDialog datePickerDialog;
    private int reminderTime = 0;
    private int flagPause = 0;

    public Contacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        listViewContacts = (ListView) view.findViewById(R.id.listViewContacts);


        if (ContextCompat.checkSelfPermission(getActivity(), READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            // We have access. Life is good.
            displayListView();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), READ_CONTACTS)
                && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), CALL_PHONE)
                && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), READ_CALL_LOG)
                && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), READ_PHONE_STATE)) {

            // We've been denied once before. Explain why we need the permission, then ask again.
            requestPermissions(new String[]{READ_CONTACTS, CALL_PHONE, READ_CALL_LOG, READ_PHONE_STATE}, REQUEST_CODE);
        } else {

            // We've never asked. Just do it.
            requestPermissions(new String[]{READ_CONTACTS, CALL_PHONE, READ_CALL_LOG, READ_PHONE_STATE}, REQUEST_CODE);
        }

        PhoneStateListener phoneStateListener = new PhoneStateListener(getContext());
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        return view;
    }

    public ArrayList<String> getNumber(ContentResolver cr) {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        // use the cursor to access the contacts
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            arrayListContacts.add(name + ":\n\n " + phone);
        }

        Collections.sort(arrayListContacts, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        return arrayListContacts;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CALL_LOG, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    displayListView();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Access Permision Denied", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void displayListView() {
        ArrayList<String> arrayListContacts = getNumber(getActivity().getContentResolver());
        ArrayAdapter<String> arrayAdapterContacts = new ArrayAdapter<String>(getActivity(), R.layout.list_view_item_contacts, arrayListContacts);
        listViewContacts.setAdapter(arrayAdapterContacts);
        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = (String) adapterView.getItemAtPosition(i);
                String[] detailsCouple = value.split(":");
                String phone = detailsCouple[1].trim();

                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(),
                            "Call failed, please try again later!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        flagPause = 1;
    }

    @Override
    public void onResume() {
        super.onResume();
        String callDetails[] = getAllCallLogs(getActivity().getContentResolver());
        //Toast.makeText(getContext(), callDetails[1] + callDetails[5], Toast.LENGTH_SHORT).show();
        int duration = Integer.valueOf(callDetails[5]);
        int type = Integer.valueOf(callDetails[3]);
        if (flagPause == 1) {
            if (duration == 0 && type == 2 || type == 5) {
                Utils.sharedPreferences = getContext().getSharedPreferences(Utils.mySharedPreferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = Utils.sharedPreferences.edit();
                String reminderNames = Utils.sharedPreferences.getString("reminders", "");
                editor.putString("reminders", "");
                editor.apply();
                editor.putString("reminders", reminderNames + "," + callDetails[1]);
                editor.apply();

                AlertDialog.Builder reminderDialog = new AlertDialog.Builder(getActivity());
                String[] types = {"Set Reminder for 15 minutes", "Set Reminder for 30 minutes"};
                reminderDialog.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0: {
                                reminderTime = 15;
                                setNotificationAlarm(reminderTime);
                                updateReminderList();
                            }
                            break;
                            case 1: {
                                reminderTime = 30;
                                setNotificationAlarm(reminderTime);
                                updateReminderList();
                            }
                            break;
                        }
                    }
                });
                reminderDialog.show();
            }
        }
        flagPause = 0;

    }

    public void updateReminderList(){
        Reminder.arrayAdapterReminders.notifyDataSetChanged();
    }

    public void setNotificationAlarm(int reminderTime) {
        Intent intentNotification = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        intentNotification.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, intentNotification, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, reminderTime);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
    }

    public static String[] getAllCallLogs(ContentResolver cr) {
        //reading all data in descending order according to DATE
        String[] callDetails = new String[6];
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Uri callUri = Uri.parse("content://call_log/calls");
        Cursor cur = cr.query(callUri, null, null, null, strOrder);
        // loop through cursor
        if (cur != null) {
            while (cur.moveToNext()) {
                callDetails[0] = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                callDetails[1] = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
                callDetails[2] = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DATE));
                callDetails[3] = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.TYPE));
                callDetails[4] = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NEW));
                callDetails[5] = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));
                break;
            }
        }

        // process log data...
        return callDetails;
    }
}
