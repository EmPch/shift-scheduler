package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String name;
    private String role; // "MANAGER" or "NORMAL"
    private int maxHours;
    private List<String> availability = new ArrayList<>();

    public Employee(String name, String role, int maxHours, List<String> availability) {
        this.name = name;
        this.role = role;
        this.maxHours = maxHours;
        this.availability = availability;
    }

    // Getters
    public String getName() { return name; }
    public String getRole() { return role; }
    public int getMaxHours() { return maxHours; }
    public List<String> getAvailability() { return availability; }
}