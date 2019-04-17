package com.example.android.findmyhcptest;
import android.provider.BaseColumns;

public final class HCPContract {

    public HCPContract() {
    }

    public static abstract class HCPEntry implements BaseColumns {
        public static final String TABLE_NAME="hcp";
        //public static final String COLUMN_HCP_ID= "hcpid";
        public static final String COLUMN_NAME= "name";
        public static final String COLUMN_ADDRESS= "address";
        public static final String COLUMN_CITY= "city";
        public static final String COLUMN_STATE= "state";
        public static final String COLUMN_ZIP= "zip";
        public static final String COLUMN_AVG_COVERED_CHARGES= "averageCoveredCharges";
        public static final String COLUMN_AVG_TOTAL_PAYMENTS= "averageTotalPayments";
        public static final String COLUMN_AVG_MEDICARE_PAYMENTS= "averageMedicarePayments";
        public static final String COLUMN_TOTAL_DISCHARGES= "totalDischarges";
    }
}