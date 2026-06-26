package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final List<Employee> employees = new ArrayList<>();
    private final List<ShiftAssignment> generatedSchedule = new ArrayList<>();

    private final List<String> definedShifts = Arrays.asList(
            "Mon Morning", "Mon Evening", "Tue Morning", "Tue Evening",
            "Wed Morning", "Wed Evening", "Thu Morning", "Thu Evening",
            "Fri Morning", "Fri Evening", "Sat Morning", "Sat Evening",
            "Sun Morning", "Sun Evening"
    );

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("employees", employees);
        model.addAttribute("definedShifts", definedShifts);
        model.addAttribute("schedule", generatedSchedule);

        // Find people with 0 hours assigned
        List<Employee> unassigned = employees.stream()
                .filter(e -> e.getAssignedHours() == 0)
                .collect(Collectors.toList());
        model.addAttribute("unassigned", unassigned);

        return "index";
    }

    @PostMapping("/add-employee")
    public String addEmployee(@RequestParam String name, @RequestParam String role,
                              @RequestParam int maxHours, @RequestParam(required = false) List<String> availability) {
        employees.add(new Employee(name, role, maxHours, availability == null ? new ArrayList<>() : availability));
        return "redirect:/";
    }

    // New: Delete specific employee
    @GetMapping("/delete-employee/{index}")
    public String deleteEmployee(@PathVariable int index) {
        employees.remove(index);
        return "redirect:/";
    }

    // New: Reset everything
    @PostMapping("/reset-all")
    public String resetAll() {
        employees.clear();
        generatedSchedule.clear();
        return "redirect:/";
    }

    @PostMapping("/generate")
    public String generateSchedule(@RequestParam int staffNeeded) {
        generatedSchedule.clear();
        employees.forEach(e -> e.setAssignedHours(0));

        for (String shift : definedShifts) {
            Employee manager = employees.stream()
                    .filter(e -> e.getRole().equals("MANAGER") && e.isAvailable(shift) && e.getAssignedHours() < e.getMaxHours())
                    .min(Comparator.comparingInt(Employee::getAssignedHours)).orElse(null);

            if (manager != null) manager.addHours(8);

            List<Employee> staff = employees.stream()
                    .filter(e -> e.getRole().equals("NORMAL") && e.isAvailable(shift) && e.getAssignedHours() < e.getMaxHours())
                    .sorted(Comparator.comparingInt(Employee::getAssignedHours))
                    .limit(staffNeeded).collect(Collectors.toList());

            staff.forEach(s -> s.addHours(8));
            generatedSchedule.add(new ShiftAssignment(shift, manager, staff));
        }
        return "redirect:/";
    }
}