package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final List<Employee> roster = new ArrayList<>();
    private final List<ScheduleEntry> schedulePool = new ArrayList<>();
    private final List<ShiftAssignment> generatedSchedule = new ArrayList<>();

    private final List<String> definedShifts = Arrays.asList(
            "Mon Morning", "Mon Evening", "Tue Morning", "Tue Evening",
            "Wed Morning", "Wed Evening", "Thu Morning", "Thu Evening",
            "Fri Morning", "Fri Evening", "Sat Morning", "Sat Evening",
            "Sun Morning", "Sun Evening"
    );

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("roster", roster);
        model.addAttribute("schedulePool", schedulePool);
        model.addAttribute("definedShifts", definedShifts);
        model.addAttribute("schedule", generatedSchedule);

        List<ScheduleEntry> unassigned = schedulePool.stream()
                .filter(e -> e.getAssignedHours() == 0)
                .collect(Collectors.toList());
        model.addAttribute("unassigned", unassigned);

        // Employees not yet added to this week's pool
        List<Employee> notInPool = roster.stream()
                .filter(e -> schedulePool.stream()
                        .noneMatch(se -> se.getEmployee() == e))
                .collect(Collectors.toList());
        model.addAttribute("notInPool", notInPool);

        return "index";
    }

    // Add to master roster (name + role only)
    @PostMapping("/add-employee")
    public String addEmployee(@RequestParam String name, @RequestParam String role) {
        roster.add(new Employee(name, role));
        return "redirect:/";
    }

    @GetMapping("/delete-employee/{index}")
    public String deleteEmployee(@PathVariable int index) {
        Employee emp = roster.get(index);
        schedulePool.removeIf(se -> se.getEmployee() == emp);
        roster.remove(index);
        return "redirect:/";
    }

    // Show form to set this week's availability for an employee
    @GetMapping("/add-to-schedule/{index}")
    public String addToScheduleForm(@PathVariable int index, Model model) {
        model.addAttribute("employee", roster.get(index));
        model.addAttribute("rosterIndex", index);
        model.addAttribute("definedShifts", definedShifts);
        return "add-to-schedule";
    }

    // Save availability and add to schedule pool
    @PostMapping("/add-to-schedule/{index}")
    public String saveToSchedule(@PathVariable int index,
                                 @RequestParam int maxHours,
                                 @RequestParam(required = false) List<String> availability) {
        Employee emp = roster.get(index);
        // Remove any existing entry for this employee (re-adding replaces it)
        schedulePool.removeIf(se -> se.getEmployee() == emp);
        schedulePool.add(new ScheduleEntry(emp, maxHours,
                availability == null ? new ArrayList<>() : availability));
        return "redirect:/";
    }

    @GetMapping("/remove-from-schedule/{index}")
    public String removeFromSchedule(@PathVariable int index) {
        schedulePool.remove(index);
        return "redirect:/";
    }

    @PostMapping("/generate")
    public String generateSchedule(@RequestParam int staffNeeded) {
        generatedSchedule.clear();
        schedulePool.forEach(e -> e.setAssignedHours(0));

        for (String shift : definedShifts) {
            ScheduleEntry manager = schedulePool.stream()
                    .filter(e -> e.getRole().equals("MANAGER")
                            && e.isAvailable(shift)
                            && e.getAssignedHours() < e.getMaxHours())
                    .min(Comparator.comparingInt(ScheduleEntry::getAssignedHours))
                    .orElse(null);
            if (manager != null) manager.addHours(8);

            List<ScheduleEntry> staff = schedulePool.stream()
                    .filter(e -> e.getRole().equals("NORMAL")
                            && e.isAvailable(shift)
                            && e.getAssignedHours() < e.getMaxHours())
                    .sorted(Comparator.comparingInt(ScheduleEntry::getAssignedHours))
                    .limit(staffNeeded)
                    .collect(Collectors.toList());
            staff.forEach(s -> s.addHours(8));

            generatedSchedule.add(new ShiftAssignment(shift, manager, staff));
        }
        return "redirect:/";
    }

    @PostMapping("/reset-all")
    public String resetAll() {
        roster.clear();
        schedulePool.clear();
        generatedSchedule.clear();
        return "redirect:/";
    }
}