package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private EmployeeRepository repo;

    private List<String> shifts = Arrays.asList("Mon Morning", "Mon Evening", "Tue Morning", "Tue Evening");

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("employees", repo.findAll());
        model.addAttribute("employee", new Employee());
        return "index";
    }

    @PostMapping("/generate")
    public String generate(Model model) {
        List<Employee> allEmployees = repo.findAll();
        Map<String, List<String>> schedule = new HashMap<>();

        // Simple Logic: Assign each person to a shift they aren't already in
        // In a real app, this is where your "Fairness + Favorites" algorithm lives
        for (Employee e : allEmployees) {
            String assignedShift = shifts.get(new Random().nextInt(shifts.size()));
            schedule.computeIfAbsent(assignedShift, k -> new ArrayList<>()).add(e.getName());
        }

        model.addAttribute("schedule", schedule);
        model.addAttribute("employees", allEmployees);
        return "index";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Employee emp) { repo.save(emp); return "redirect:/"; }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) { repo.deleteById(id); return "redirect:/"; }
}