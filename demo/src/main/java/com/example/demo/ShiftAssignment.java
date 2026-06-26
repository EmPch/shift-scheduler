package com.example.demo;

import java.util.List;

public class ShiftAssignment {
    private String shiftName;
    private Employee manager;
    private List<Employee> normalStaff;

    public ShiftAssignment(String shiftName, Employee manager, List<Employee> normalStaff) {
        this.shiftName = shiftName;
        this.manager = manager;
        this.normalStaff = normalStaff;
    }

    // Getters so the HTML can read the data
    public String getShiftName() { return shiftName; }
    public Employee getManager() { return manager; }
    public List<Employee> getNormalStaff() { return normalStaff; }
}