package com.kit.backpackers.project_kit.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Expedition implements Parcelable {
    public String IdExpedition;
    public String Name;
    public String Description;
    public String Type;
    public String Status;
    public String UserId;
    public String StartDate;
    public String StartTime;
    public String EndDate;
    public String EndTime;
    public String Place;


    public Expedition(String idExpedition, String name, String description, String type, String status, String userId, String startDate, String startTime, String endDate, String endTime, String place) {
        IdExpedition = idExpedition;
        Name = name;
        Description = description;
        Type = type;
        Status = status;
        UserId = userId;
        StartDate = startDate;
        StartTime = startTime;
        EndDate = endDate;
        EndTime = endTime;
        Place = place;
    }

    public void setIdExpedition(String idExpedition) {
        IdExpedition = idExpedition;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getIdExpedition() {
        return IdExpedition;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getType() {
        return Type;
    }

    public String getStatus() {
        return Status;
    }

    public String getUserId() {
        return UserId;
    }

    public String getStartDate() {
        return StartDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getEndDate() {
        return EndDate;
    }

    public String getEndTime() {
        return EndTime;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
