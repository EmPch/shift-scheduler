package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class HomeController {
    private List<Employee> employees = new ArrayList<>();
    private List<String> shifts = Arrays.asList("Mon Morning", "Mon Evening", "Tue Morning", "Tue Evening");

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("employees", employees);
        model.addAttribute("definedShifts", shifts);
        return "index";
    }

    @PostMapping("/add-employee")
    public String add(@RequestParam String name, @RequestParam String role,
                      @RequestParam int maxHours, @RequestParam(required = false) List<String> availability) {
        employees.add(new Employee(name, role, maxHours, availability != null ? availability : new ArrayList<>()));
        return "redirect:/";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam int staffNeeded, Model model) {
        // Basic Scheduling Logic
        Map<String, List<String>> schedule = new HashMap<>();
        for (String shift : shifts) {
            schedule.put(shift, new ArrayList<>());
            for (Employee e : employees) {
                if (e.getAvailability().contains(shift)) {
                    schedule.get(shift).add(e.getName());
                }
            }
        }
        model.addAttribute("schedule", schedule);
        model.addAttribute("employees", employees);
        return "index";
    }
}