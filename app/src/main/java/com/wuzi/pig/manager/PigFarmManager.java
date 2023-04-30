package com.wuzi.pig.manager;

public class PigFarmManager {

    private static PigFarmManager sInstance = null;

    private PigFarmManager() {

    }

    public static PigFarmManager getInstance() {
        if (sInstance == null) {
            synchronized (PigFarmManager.class) {
                if (sInstance == null) {
                    sInstance = new PigFarmManager();
                }
            }
        }
        return sInstance;
    }
}
