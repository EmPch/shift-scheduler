package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    private final List<Employee> employees = new ArrayList<>();
    private final List<ShiftAssignment> generatedSchedule = new ArrayList<>();

    // UPDATED: Full Monday through Sunday, Morning and Evening shifts
    private final List<String> definedShifts = Arrays.asList(
            "Monday Morning", "Monday Evening",
            "Tuesday Morning", "Tuesday Evening",
            "Wednesday Morning", "Wednesday Evening",
            "Thursday Morning", "Thursday Evening",
            "Friday Morning", "Friday Evening",
            "Saturday Morning", "Saturday Evening",
            "Sunday Morning", "Sunday Evening"
    );

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("employees", employees);
        model.addAttribute("definedShifts", definedShifts);
        model.addAttribute("schedule", generatedSchedule);
        return "index";
    }

    @PostMapping("/add-employee")
    public String addEmployee(@RequestParam String name,
                              @RequestParam String role,
                              @RequestParam int maxHours,
                              @RequestParam(required = false) List<String> availability) {
        if (availability == null) availability = new ArrayList<>();
        employees.add(new Employee(name, role, maxHours, availability));
        return "redirect:/";
    }

    // THE BRAIN OF THE APP (UPDATED FOR DYNAMIC STAFFING)
    @PostMapping("/generate")
    public String generateSchedule(@RequestParam int staffNeeded) { // <-- Catches the number from your webpage
        generatedSchedule.clear();

        for (Employee e : employees) {
            e.setAssignedHours(0);
        }

        int SHIFT_LENGTH = 8;

        for (String shift : definedShifts) {

            // 1. Find exactly ONE Manager
            Employee chosenManager = employees.stream()
                    .filter(e -> e.getRole().equals("MANAGER"))
                    .filter(e -> e.isAvailable(shift))
                    .filter(e -> e.getAssignedHours() + SHIFT_LENGTH <= e.getMaxHours())
                    .min(Comparator.comparingInt(Employee::getAssignedHours))
                    .orElse(null);

            if (chosenManager != null) {
                chosenManager.addHours(SHIFT_LENGTH);
            }

            // 2. Find the Normal Staff (Using your dynamic number!)
            List<Employee> chosenStaff = employees.stream()
                    .filter(e -> e.getRole().equals("NORMAL"))
                    .filter(e -> e.isAvailable(shift))
                    .filter(e -> e.getAssignedHours() + SHIFT_LENGTH <= e.getMaxHours())
                    .sorted(Comparator.comparingInt(Employee::getAssignedHours))
                    .limit(staffNeeded) // <-- It now cuts off at whatever number you typed in!
                    .toList();

            for (Employee staff : chosenStaff) {
                staff.addHours(SHIFT_LENGTH);
            }

            generatedSchedule.add(new ShiftAssignment(shift, chosenManager, chosenStaff));
        }

        return "redirect:/";
    }
}