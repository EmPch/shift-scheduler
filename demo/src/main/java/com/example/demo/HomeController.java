package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final EmployeeRepository employeeRepo;
    private final ScheduleEntryRepository scheduleEntryRepo;
    private final List<ShiftAssignment> generatedSchedule = new ArrayList<>();

    private final List<String> definedShifts = Arrays.asList(
            "Mon Morning", "Mon Evening", "Tue Morning", "Tue Evening",
            "Wed Morning", "Wed Evening", "Thu Morning", "Thu Evening",
            "Fri Morning", "Fri Evening", "Sat Morning", "Sat Evening",
            "Sun Morning", "Sun Evening"
    );

    public HomeController(EmployeeRepository employeeRepo, ScheduleEntryRepository scheduleEntryRepo) {
        this.employeeRepo = employeeRepo;
        this.scheduleEntryRepo = scheduleEntryRepo;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<Employee> roster = employeeRepo.findAll();
        List<ScheduleEntry> schedulePool = scheduleEntryRepo.findAll();

        List<ScheduleEntry> unassigned = schedulePool.stream()
                .filter(e -> e.getAssignedHours() == 0)
                .collect(Collectors.toList());

        List<Employee> notInPool = roster.stream()
                .filter(e -> schedulePool.stream()
                        .noneMatch(se -> se.getEmployee().getId().equals(e.getId())))
                .collect(Collectors.toList());

        model.addAttribute("roster", roster);
        model.addAttribute("schedulePool", schedulePool);
        model.addAttribute("definedShifts", definedShifts);
        model.addAttribute("schedule", generatedSchedule);
        model.addAttribute("unassigned", unassigned);
        model.addAttribute("notInPool", notInPool);

        return "index";
    }

    @PostMapping("/add-employee")
    public String addEmployee(@RequestParam String name, @RequestParam String role) {
        employeeRepo.save(new Employee(name, role));
        return "redirect:/";
    }

    @GetMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        scheduleEntryRepo.findAll().stream()
                .filter(se -> se.getEmployee().getId().equals(id))
                .forEach(scheduleEntryRepo::delete);
        employeeRepo.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/add-to-schedule/{id}")
    public String addToScheduleForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeRepo.findById(id).orElseThrow());
        model.addAttribute("employeeId", id);
        model.addAttribute("definedShifts", definedShifts);
        return "add-to-schedule";
    }

    @PostMapping("/add-to-schedule/{id}")
    public String saveToSchedule(@PathVariable Long id,
                                 @RequestParam int maxHours,
                                 @RequestParam(required = false) List<String> availability) {
        Employee emp = employeeRepo.findById(id).orElseThrow();
        scheduleEntryRepo.findAll().stream()
                .filter(se -> se.getEmployee().getId().equals(id))
                .forEach(scheduleEntryRepo::delete);
        scheduleEntryRepo.save(new ScheduleEntry(emp, maxHours,
                availability == null ? new ArrayList<>() : availability));
        return "redirect:/";
    }

    @GetMapping("/remove-from-schedule/{id}")
    public String removeFromSchedule(@PathVariable Long id) {
        scheduleEntryRepo.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/generate")
    public String generateSchedule(@RequestParam int staffNeeded) {
        generatedSchedule.clear();
        List<ScheduleEntry> schedulePool = scheduleEntryRepo.findAll();
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
        scheduleEntryRepo.deleteAll();
        employeeRepo.deleteAll();
        generatedSchedule.clear();
        return "redirect:/";
    }
}