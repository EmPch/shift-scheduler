package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String name;
    private String role;
    private int maxHours;
    private List<String> availability;

    // NEW: Keeps track of scheduled hours while the algorithm runs
    private int assignedHours = 0;

    public Employee(String name, String role, int maxHours, List<String> availability) {
        this.name = name;
        this.role = role;
        this.maxHours = maxHours;
        this.availability = availability != null ? availability : new ArrayList<>();
    }

    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setMaxHours(int maxHours) { this.maxHours = maxHours; }
    public void setAvailability(List<String> availability) { this.availability = availability; }

    public String getName() { return name; }
    public String getRole() { return role; }
    public int getMaxHours() { return maxHours; }
    public List<String> getAvailability() { return availability; }
    public boolean isAvailable(String shift) { return availability.contains(shift); }

    // NEW: Methods for the algorithm to manage hours
    public int getAssignedHours() { return assignedHours; }
    public void setAssignedHours(int hours) { this.assignedHours = hours; }
    public void addHours(int hours) { this.assignedHours += hours; }
}