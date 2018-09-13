package com.example.nathan.loadtracker.load;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by nathanabramyk on 2017-10-15.
 */

public class Load {

    private int id;
    private String title;
    private String driver;
    private String unit_id;
    private String material;
    private String time_loaded;
    private String date_loaded;
    private String created;
    private String modified;
    private String company_name;

    public Load() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriver() {
        return this.driver;
    }

    public String getUnitId() {
        return unit_id;
    }

    public void setUnitId(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getTimeLoaded() {
        return time_loaded;
    }

    public void setTimeLoaded (String time_loaded) {
        this.time_loaded = time_loaded;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated (String created) {
        this.created = created;
    }

    public String getDateLoaded() {
        return date_loaded;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public void setDateLoaded(String date_loaded) {
        this.date_loaded = date_loaded;
    }

    public String toString() {
        return "ID: " + id + ", Title: " + title + ", Driver: " + driver + ", Unit ID: " + unit_id + ", Material: " + material + ", Company Name: " + company_name + ", Time Loaded: " + time_loaded + ", Date Loaded: " + date_loaded + ", Created: " + created + ", Modified: " + modified;
    }

    public String getCompanyName() {
        return company_name;
    }

    public void setCompanyName(String company_name) {
        this.company_name = company_name;
    }
}
