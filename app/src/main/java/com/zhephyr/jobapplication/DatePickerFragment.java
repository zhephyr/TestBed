package com.zhephyr.jobapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment
                                implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog.OnDateSetListener mDateSetListener;

    public DatePickerFragment() {}

    public DatePickerFragment(DatePickerDialog.OnDateSetListener callback) {
        mDateSetListener = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {}
}