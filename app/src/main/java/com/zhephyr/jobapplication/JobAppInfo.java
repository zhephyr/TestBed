package com.zhephyr.jobapplication;

import java.util.ArrayList;
import java.util.List;

public class JobAppInfo {
    private String fName;
    private String lName;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phoneNum;
    private String phoneType;
    private String email;
    private List<WorkHistory> WrkHistory;

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public List<WorkHistory> getWrkHistory() {
        return WrkHistory;
    }

    public JobAppInfo(String fName, String lName, String address, String city, String state,
                      String zip, String phoneNum, String phoneType, String email, ArrayList<WorkHistory> wrkHist) {
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNum = phoneNum;
        this.phoneType = phoneType;
        this.email = email;
        this.WrkHistory = wrkHist;
    }
}
