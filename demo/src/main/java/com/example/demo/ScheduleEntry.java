package com.example.demo;

import java.util.List;

public class ScheduleEntry {
    private Employee employee;
    private int maxHours;
    private List<String> availability;
    private int assignedHours = 0;

    public ScheduleEntry(Employee employee, int maxHours, List<String> availability) {
        this.employee = employee;
        this.maxHours = maxHours;
        this.availability = availability;
    }

    public Employee getEmployee() { return employee; }
    public String getName() { return employee.getName(); }
    public String getRole() { return employee.getRole(); }
    public int getMaxHours() { return maxHours; }
    public List<String> getAvailability() { return availability; }
    public int getAssignedHours() { return assignedHours; }
    public void setAssignedHours(int h) { this.assignedHours = h; }
    public void addHours(int h) { this.assignedHours += h; }
    public boolean isAvailable(String shift) { return availability.contains(shift); }
}