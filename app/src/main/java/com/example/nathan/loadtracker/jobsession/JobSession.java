package com.example.nathan.loadtracker.jobsession;

/**
 * Created by Nathan on 2017-10-13.
 */

public class JobSession {

    private int id;
    private String jobTitle;
    private String startDate;
    private String closedDate;
    private String created;

    public int getTotalLoads() {
        return totalLoads;
    }

    public void setTotalLoads(int totalLoads) {
        this.totalLoads = totalLoads;
    }

    private int totalLoads;

    public JobSession(){}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String toString() {
        return "ID: " + id + ", Job Title: " + jobTitle + ", Created: " + created + ", Closed: " + closedDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }
}
