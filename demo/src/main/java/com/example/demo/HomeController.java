package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private EmployeeRepository repo;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("employees", repo.findAll());
        model.addAttribute("employee", new Employee());
        return "index";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Employee emp) { repo.save(emp); return "redirect:/"; }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) { repo.deleteById(id); return "redirect:/"; }

    // We will handle Edit in the next step
}