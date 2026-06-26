package com.example.demo;

import java.util.List;

public class ShiftAssignment {
    private String shiftName;
    private ScheduleEntry manager;
    private List<ScheduleEntry> normalStaff;

    public ShiftAssignment(String shiftName, ScheduleEntry manager, List<ScheduleEntry> normalStaff) {
        this.shiftName = shiftName;
        this.manager = manager;
        this.normalStaff = normalStaff;
    }

    public String getShiftName() { return shiftName; }
    public ScheduleEntry getManager() { return manager; }
    public List<ScheduleEntry> getNormalStaff() { return normalStaff; }
}