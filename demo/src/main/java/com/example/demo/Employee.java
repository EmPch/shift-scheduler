package com.example.demo;

public class Employee {
    private String name;
    private String role;

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() { return name; }
    public String getRole() { return role; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
}