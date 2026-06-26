package com.example.demo;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "schedule_entries")
public class ScheduleEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private int maxHours;
    private int assignedHours = 0;

    @ElementCollection
    @CollectionTable(name = "schedule_availability", joinColumns = @JoinColumn(name = "entry_id"))
    @Column(name = "shift")
    private List<String> availability;

    public ScheduleEntry() {}

    public ScheduleEntry(Employee employee, int maxHours, List<String> availability) {
        this.employee = employee;
        this.maxHours = maxHours;
        this.availability = availability;
    }

    public Long getId() { return id; }
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