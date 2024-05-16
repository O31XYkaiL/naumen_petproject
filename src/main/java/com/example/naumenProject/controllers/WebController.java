package com.example.naumenProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;
import java.util.stream.Collectors;

import com.example.naumenProject.services.ProjectService;

import org.springframework.ui.Model;
import com.example.naumenProject.models.User;
import com.example.naumenProject.models.Project;
import com.example.naumenProject.repositories.UserRepository;

import org.springframework.security.core.Authentication;

@Controller
public class WebController
{
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
        var projectNames = projects.stream().map(Project::getProjectName).collect(Collectors.toList());

        model.addAttribute("user", currentUser);
        model.addAttribute("projects", projectNames);

        return "projects";
    }

    @PostMapping(value = "/createProject")
    public String createProject(@RequestParam("name") String name, @RequestParam("description") String description, Authentication authentication) {

        Project project = new Project(UUID.randomUUID().getMostSignificantBits(), name, description, "", "", "", "", "");

        projectService.createProject(project);

        return "redirect:/projects";
    }

    @GetMapping(value = "/ratings")
    public String getRatingsPage(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);
        var projects = projectService.getAllProjects();

        var projectNamesToRatings = projects.stream().collect(Collectors.toMap(Project::getProjectName, Project::getProjectRating));
        var sortedProjects = projectNamesToRatings.entrySet().stream().sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue())).collect(Collectors.toList());

        model.addAttribute("user", currentUser);
        model.addAttribute("projects", sortedProjects);

        return "ratings";
    }

    @PostMapping(value = "/updateRating")
    public String updateRating(@RequestParam("projectName") String projectName, @RequestParam("rating") Integer rating, Authentication authentication) {
        var projects = projectService.getAllProjects();
        var project = projects.stream().filter(p -> p.getProjectName().equals(projectName)).findFirst().orElse(null);

        if (project != null) {
            project.setProjectRating(rating);
            projectService.updateProject(project);
        }

        return "redirect:/ratings";
    }
}
