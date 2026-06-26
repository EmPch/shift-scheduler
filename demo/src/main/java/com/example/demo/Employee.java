package com.example.demo;
import jakarta.persistence.*;
import java.util.*;

@Entity
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String role; // "MANAGER" or "NORMAL"
    private int maxHours = 40;

    @ElementCollection
    private Set<String> favorites = new HashSet<>();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public int getMaxHours() { return maxHours; }
    public void setMaxHours(int maxHours) { this.maxHours = maxHours; }
    public Set<String> getFavorites() { return favorites; }
    public void setFavorites(Set<String> favorites) { this.favorites = favorites; }
}