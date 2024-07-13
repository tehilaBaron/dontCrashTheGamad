package com.example.android_ex2.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecordList {
    ArrayList<Record> records = new ArrayList<>();

    public ArrayList<Record> getRecords() {
        return records;
    }

    public RecordList setRecords(ArrayList<Record> records) {
        this.records = records;
        return this;
    }

    public RecordList addRecord(Record record) {
        this.records.add(record);
        return this;
    }

    public ArrayList<Record> getSortedRecords() {
        records.sort(new RecordPointsComparator());
        int limit = Math.min(records.size(), 10);
        return new ArrayList<>(records.subList(0, limit));
    }

    public static class RecordPointsComparator implements Comparator<Record> {
        @Override
        public int compare(Record r1, Record r2) {
            return Integer.compare(r2.getPoints(), r1.getPoints());
        }
    }

    public RecordList() {
    }
}
