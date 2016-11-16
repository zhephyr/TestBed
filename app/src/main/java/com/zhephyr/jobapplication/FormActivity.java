package com.zhephyr.jobapplication;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Struct;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FormActivity extends AppCompatActivity {

    EditText fNameEntry, lNameEntry, addressEntry, cityEntry, stateEntry, zipEntry, phoneNumEntry, emailEntry;
    TextView positionEntry, compEntry, startDateEntry, endDateEntry, dutiesEntry;
    List<EditText> editFields = new ArrayList<>();

    Spinner phoneType;
    Button addWrkHist, submitBtn;
    PhoneNumberFormattingTextWatcher phoneWatcher = new PhoneNumberFormattingTextWatcher();
    SimpleDateFormat dateFormat;

    ArrayList<WorkHistory> wrkHistory = new ArrayList<>();
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        View mainView = (View) findViewById(R.id.mainForm);
        fNameEntry = (EditText) findViewById(R.id.fName);
        lNameEntry = (EditText) findViewById(R.id.lName);
        addressEntry = (EditText) findViewById(R.id.addrStreet);
        cityEntry = (EditText) findViewById(R.id.addrCity);
        stateEntry = (EditText) findViewById(R.id.addrState);
        zipEntry = (EditText) findViewById(R.id.addrZip);
        phoneNumEntry = (EditText) findViewById(R.id.phoneNumber);
        phoneNumEntry.addTextChangedListener(phoneWatcher);
        emailEntry = (EditText) findViewById(R.id.email);
        phoneType = (Spinner) findViewById(R.id.phoneSpinner);

        editFields.add(fNameEntry);
        editFields.add(lNameEntry);
        editFields.add(addressEntry);
        editFields.add(cityEntry);
        editFields.add(stateEntry);
        editFields.add(zipEntry);
        editFields.add(phoneNumEntry);
        editFields.add(emailEntry);

        for (final EditText field : editFields) {
            field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && field.getText().toString().matches("")) {
                        field.setError("This field cannot be empty.");
                        Toast errToast = Toast.makeText(v.getContext(), "A required field was left empty.", Toast.LENGTH_LONG);
                        errToast.show();
                    }
                }
            });
        }

        addWrkHist = (Button) findViewById(R.id.addWrkHist);
        addWrkHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorkHistory(v);
            }
        });
        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveJobAppData();
            }
        });

        final Snackbar timerBar = Snackbar.make(mainView, "Time left to finish app: ", Snackbar.LENGTH_INDEFINITE);

        new CountDownTimer(300000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerBar.setText("Time left to finish app: " + String.format(Locale.US, "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                ));
            }

            @Override
            public void onFinish() {
                timerBar.dismiss();
            }
        }.start();

        timerBar.show();
    }

    private void saveJobAppData() {
        if (!positionEntry.getText().toString().matches("") && validWorkHistory()) {
            if (!saveWrkHist()) {
                Toast errToast = Toast.makeText(getApplicationContext(), "Could not save current work history. Check if valid and try again.", Toast.LENGTH_LONG);
                errToast.show();
            }
        }

        try {
            String fName = fNameEntry.getText().toString();
            String lName = lNameEntry.getText().toString();
            String address = addressEntry.getText().toString();
            String addrCity = cityEntry.getText().toString();
            String addrState = stateEntry.getText().toString();
            String addrZip = zipEntry.getText().toString();
            String telephone = zipEntry.getText().toString();
            String teleType = phoneType.getSelectedItem().toString();
            String email = emailEntry.getText().toString();
            JobAppInfo appInfo = new JobAppInfo(fName, lName, address, addrCity,
                    addrState, addrZip, telephone, teleType, email, wrkHistory);

            FileOutputStream fileOutputStream = new FileOutputStream("/applicant.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(appInfo);
            out.close();
        } catch (Exception ex) {
            Toast errToast = Toast.makeText(getApplicationContext(), "Error saving data.", Toast.LENGTH_LONG);
            errToast.show();
            ex.printStackTrace();
        }
    }

    private void addWorkHistory(View v) {
        if (findViewById(R.id.wrkHist) == null)
            createWrkHist();
        else if (validWorkHistory()){
            if (saveWrkHist()){
                displayWrkHist();
                resetWrkHist();
            } else {
                Toast errToast = Toast.makeText(getApplicationContext(), "Could not save work history. Check if all fields are valid.", Toast.LENGTH_LONG);
                errToast.show();
                return;
            }
        } else {
            Toast errToast = Toast.makeText(v.getContext(), "Work history is not valid. Please check errors.", Toast.LENGTH_LONG);
            errToast.show();
        }
    }

    private void createWrkHist() {
        ViewGroup editHistGrp = (ViewGroup) findViewById(R.id.editWrkHistory);

        LayoutInflater inflator = LayoutInflater.from(editHistGrp.getContext());
        View v = inflator.inflate(R.layout.work_history_table, null);
        editHistGrp.addView(v);

        positionEntry = (EditText) findViewById(R.id.wrkPosition);
        compEntry = (EditText) findViewById(R.id.wrkCompany);
        startDateEntry = (TextView) findViewById(R.id.wrkStartDate);
        endDateEntry = (TextView) findViewById(R.id.wrkEndDate);
        dutiesEntry = (EditText) findViewById(R.id.wrkDuties);

        startDateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogFragment dateFragment = new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, dayOfMonth);
                        startDateEntry.setText(dateFormat.format(cal.getTime()));
                    }
                });
                dateFragment.show(getFragmentManager(), "start_datepicker");
            }
        });
        endDateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogFragment dateFragment = new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, dayOfMonth);
                        endDateEntry.setText(dateFormat.format(cal.getTime()));
                    }
                });
                dateFragment.show(getFragmentManager(), "end_datepicker");
            }
        });

        findViewById(R.id.wrkPosition).requestFocus();
    }

    private boolean validWorkHistory() {
        if (positionEntry.getText().toString().matches("") || compEntry.getText().toString().matches("") ||
                startDateEntry.getText().toString().matches("") || dutiesEntry.getText().toString().matches("")) {
            Toast errToast = Toast.makeText(getApplicationContext(), "Work History is missing required fields.", Toast.LENGTH_LONG);
            errToast.show();
            return false;
        } else {
            return true;
        }
    }

    private Boolean saveWrkHist() {
        String posiData = positionEntry.getText().toString();
        String compData = compEntry.getText().toString();
        String dutiData = dutiesEntry.getText().toString();
        String startDateStr, endDateStr = null;
        Date startDate, endDate;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        if (!startDateEntry.getText().toString().matches("")) {
            startDateStr = startDateEntry.getText().toString();
        } else {
            return false;
        }
        if (!endDateEntry.getText().toString().matches("")) {
            endDateStr = endDateEntry.getText().toString();
        }

        try {
            startDate = format.parse(startDateStr);
            if (endDateStr != null) {
                endDate = format.parse(endDateStr);
            } else {
                endDate = null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        wrkHistory.add(new WorkHistory(posiData, compData, startDate, endDate, dutiData));
        return true;
    }

    private void displayWrkHist() {

        for (;index < wrkHistory.size(); index++) {
            if (createDisplay(index)) {
                fillDisplay(wrkHistory.get(index), index * 10);
            } else {
                Toast errToast = Toast.makeText(getApplicationContext(), "Unable to display work History", Toast.LENGTH_LONG);
                errToast.show();
            }
        }
    }

    private Boolean createDisplay(int index) {
        try {
            ViewGroup displayHistoryGrp = (ViewGroup) findViewById(R.id.displayHistory);
            LayoutInflater inflator = LayoutInflater.from(displayHistoryGrp.getContext());
            View dispHistory = inflator.inflate(R.layout.display_work_history, null);
            displayHistoryGrp.addView(dispHistory);

            TextView dispPosition = (TextView) findViewById(R.id.dispWrkPosition);
            TextView dispCompany = (TextView) findViewById(R.id.dispWrkCompany);
            TextView dispStart = (TextView) findViewById(R.id.dispWrkStartDate);
            TextView dispEnd = (TextView) findViewById(R.id.dispWrkEndDate);
            TextView dispDuties = (TextView) findViewById(R.id.dispWrkDuties);

            dispHistory.setId(index);
            int subindex = index * 10;
            dispPosition.setId(subindex + 1);
            dispCompany.setId(subindex + 2);
            dispStart.setId(subindex + 3);
            dispEnd.setId(subindex + 4);
            dispDuties.setId(subindex + 5);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void fillDisplay(WorkHistory entry, int subindex) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        TextView dispPosition = (TextView) findViewById(subindex + 1);
        TextView dispCompany = (TextView) findViewById(subindex + 2);
        TextView dispStart = (TextView) findViewById(subindex + 3);
        TextView dispEnd = (TextView) findViewById(subindex + 4);
        TextView dispDuties = (TextView) findViewById(subindex + 5);

        dispPosition.setText(entry.position);
        dispCompany.setText(entry.company);
        dispStart.setText(formatter.format(entry.startDate));
        dispEnd.setText(formatter.format(entry.endDate));
        dispDuties.setText(entry.duties);
    }

    private void resetWrkHist() {
        positionEntry.setText(null);
        compEntry.setText(null);
        startDateEntry.setText(null);
        endDateEntry.setText(null);
        dutiesEntry.setText(null);
    }
}
