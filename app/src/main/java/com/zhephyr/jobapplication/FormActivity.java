package com.zhephyr.jobapplication;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.widget.RelativeLayout.BELOW;

public class FormActivity extends AppCompatActivity {

    public class JobAppInfo {
        private String fName;
        private String lName;
        private String address;
        private String city;
        private String state;
        private String zip;
        private String phoneNum;
        private String email;
        private List<WorkHistory> WrkHistory;

        public JobAppInfo(String fName, String lName, String address, String city, String state,
                          String zip, String phoneNum, String email, ArrayList<WorkHistory> wrkHist) {
            this.fName = fName;
            this.lName = lName;
            this.address = address;
            this.city = city;
            this.state = state;
            this.zip = zip;
            this.phoneNum = phoneNum;
            this.email = email;
            this.WrkHistory = wrkHist;
        }
    }

    public class WorkHistory {
        private String position;
        private String company;
        private Date startDate;
        private Date endDate;
        private String duties;

        public WorkHistory(String pos, String comp, Date sDate, Date eDate, String duties) {
            this.position = pos;
            this.company = comp;
            this.startDate = sDate;
            this.endDate = eDate;
            this.duties = duties;
        }
    }

    EditText fNameEntry, lNameEntry, addressEntry, cityEntry, stateEntry, zipEntry, phoneNumEntry, emailEntry;
    TextView positionEntry, compEntry, startDateEntry, endDateEntry, dutiesEntry;
    List<EditText> editFields = new ArrayList<>();

    Spinner phoneType;
    Button addWrkHist;
    PhoneNumberFormattingTextWatcher phoneWatcher = new PhoneNumberFormattingTextWatcher();
    SimpleDateFormat dateFormat;

    List<WorkHistory> wrkHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

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
    }

    private void addWorkHistory(View v) {
        if (findViewById(R.id.wrkHist) == null)
            createWrkHist();
        else if (validWorkHistory()){
            saveWrkHist();
//            displayWrkHist();
            createWrkHist();
        } else {

        }
    }

    private void createWrkHist() {
        View addBtn = findViewById(R.id.addWrkHist);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) addBtn.getLayoutParams();
        int aboveID = params.getRule(BELOW);
        ViewGroup mainView = (ViewGroup) findViewById(R.id.mainForm);

        LayoutInflater inflator = LayoutInflater.from(mainView.getContext());
        View v = inflator.inflate(R.layout.work_history_table, null);
        RelativeLayout.LayoutParams insertParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        insertParams.addRule(BELOW, aboveID);
        v.setLayoutParams(insertParams);
        mainView.addView(v);

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
                        startDateEntry.setText(dateFormat.format(cal.getTime()));
                    }
                });
                dateFragment.show(getFragmentManager(), "end_datepicker");
            }
        });

        params.addRule(BELOW, R.id.wrkHist);
        addBtn.setLayoutParams(params);
        findViewById(R.id.wrkPosition).requestFocus();
    }

    private boolean validWorkHistory() {
        if (positionEntry.getText().toString().matches("") || compEntry.getText().toString().matches("") ||
                startDateEntry.getText().toString().matches("") || dutiesEntry.getText().toString().matches("")) {
            return false;
        } else {
            Toast errToast = Toast.makeText(getApplicationContext(), "Work History is missing required fields.", Toast.LENGTH_LONG);
            errToast.show();
            return true;
        }
    }

    private void saveWrkHist() {
        String posiData = positionEntry.getText().toString();
        String compData = compEntry.getText().toString();
        String dutiData = dutiesEntry.getTransitionName().toString();

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String dateStr = startDateEntry.getText().toString();
        try {
            Date startDate = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
