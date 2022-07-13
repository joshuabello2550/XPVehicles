package com.example.xpvehicles.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Locker")
public class Locker extends ParseObject {

    public static final String KEY_LOCKER_NUMBER =  "lockerNumber";
    public static final String KEY_AVAILABILITY =  "availability";
    public static final String KEY_STORAGE_CENTER =  "storageCenter";
    public static final String KEY_LOCKER_CODE =  "lockerCode";

    public Boolean getAvailability() {
        return getBoolean(KEY_AVAILABILITY);
    }

    public void setAvailability(Boolean availability) {
        put(KEY_AVAILABILITY, availability);
    }

    public int getLockerNumber() {
        return getInt(KEY_LOCKER_NUMBER);
    }

    public void setLockerNumber(int lockerNumber) {
        put(KEY_LOCKER_NUMBER, lockerNumber);
    }

    public StorageCenter getStorageCenter() {
        return (StorageCenter) getParseObject(KEY_STORAGE_CENTER);
    }

    public void setStorageCenter(StorageCenter storageCenter) {
        put(KEY_STORAGE_CENTER, storageCenter);
    }

    public Number getLockerCode(int lockerCode) {
        return getInt(KEY_LOCKER_CODE);
    }

    public void setLockerCode(int lockerCode) {
        put(KEY_LOCKER_CODE, lockerCode);
    }
}