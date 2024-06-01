package com.example.naumenProject.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.naumenProject.models.Project;
import com.example.naumenProject.models.User;
import com.example.naumenProject.repositories.UserRepository;
import com.example.naumenProject.services.ProjectService;

@Controller
public class WebController {
    private final UserRepository userRepository;
    private final ProjectService projectService;

    @Autowired
    public WebController(UserRepository userRepository, ProjectService projectService) {
        this.userRepository = userRepository;
        this.projectService = projectService;
    }

    @GetMapping(value = "/")
    public String getMainPage(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        model.addAttribute("user", currentUser);

        return "index";
    }

    @GetMapping(value = "/projects")
    public String getProjectsPage(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        var projects = projectService.getAllProjects();

        model.addAttribute("title", "Проекты");
        model.addAttribute("user", currentUser);
        model.addAttribute("projects", projects);

        return "projects";
    }

    @GetMapping(value = "/projectsByUser")
    public String getProjectsByUser(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        var userProjects = projectService.getProjectsByUser(username);

        model.addAttribute("title", "Проекты от " + currentUser.getUsername());
        model.addAttribute("user", currentUser);
        model.addAttribute("projects", userProjects);

        return "projects";
    }

    @PostMapping(value = "/createProject")
    public String createProject(@RequestParam("name") String name, @RequestParam("description") String description,
            Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        Project project = new Project(UUID.randomUUID().getMostSignificantBits(), username, name, description, "", "", "", "",
                "");

        projectService.createProject(project);

        return "redirect:/projects";
    }

    @PostMapping(value = "/deleteProject")
    public String deleteProject(@RequestParam("id") Long id, Authentication authentication) {
        projectService.deleteProject(id);

        return "redirect:/projects";
    }

    @GetMapping(value = "/ratings")
    public String getRatingsPage(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);
        var projects = projectService.getProjectsSortedByRating();

        model.addAttribute("user", currentUser);
        model.addAttribute("projects", projects);

        return "ratings";
    }

    @PostMapping(value = "/updateRating")
    public String updateRating(@RequestParam("projectName") String projectName, @RequestParam("rating") Integer rating,
            Authentication authentication) {
        var project = projectService.getProjectByName(projectName);

        if (project != null) {
            project.setProjectRating(rating);
            projectService.updateProject(project);
        }

        return "redirect:/ratings";
    }

    @GetMapping(value = "/search")
    public String search(@RequestParam(value = "query", required = false) String query, Model model,
            Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        List<Project> filteredProjects;
        if (query != null && !query.isEmpty()) {
            filteredProjects = projectService.searchProjects(query);
        } else {
            filteredProjects = projectService.getAllProjects();
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("projects", filteredProjects);

        return "search";
    }
}
