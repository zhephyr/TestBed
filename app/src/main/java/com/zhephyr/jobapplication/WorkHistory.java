package com.zhephyr.jobapplication;

import java.util.Date;

public class WorkHistory {
    public String position;
    public String company;
    public Date startDate;
    public Date endDate;
    public String duties;

    public WorkHistory(String pos, String comp, Date sDate, Date eDate, String duties) {
        this.position = pos;
        this.company = comp;
        this.startDate = sDate;
        this.endDate = eDate;
        this.duties = duties;
    }
}
