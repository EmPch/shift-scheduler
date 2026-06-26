package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String name;
    private String role;
    private int maxHours;
    private int assignedHours = 0;
    private List<String> availability = new ArrayList<>();

    public Employee(String name, String role, int maxHours, List<String> availability) {
        this.name = name;
        this.role = role;
        this.maxHours = maxHours;
        this.availability = availability;
    }

    public String getName() { return name; }
    public String getRole() { return role; }
    public int getMaxHours() { return maxHours; }
    public int getAssignedHours() { return assignedHours; }
    public List<String> getAvailability() { return availability; }

    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setMaxHours(int maxHours) { this.maxHours = maxHours; }
    public void setAssignedHours(int h) { this.assignedHours = h; }
    public void setAvailability(List<String> availability) { this.availability = availability; }

    public void addHours(int hours) { this.assignedHours += hours; }

    public boolean isAvailable(String shift) { return availability.contains(shift); }
}