package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String role;

    public Employee() {}

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
}