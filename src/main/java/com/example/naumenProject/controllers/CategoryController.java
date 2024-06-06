package com.example.naumenProject.controllers;

import com.example.naumenProject.repositories.ProjectRepository;
import com.example.naumenProject.repositories.UserRepository;
import com.example.naumenProject.services.ProjectService;
import com.example.naumenProject.services.TeamService;
import com.example.naumenProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {
    private final UserRepository userRepository;
    public final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final TeamService teamService;
    private final UserService userService;

    @Autowired
    public CategoryController(UserRepository userRepository, ProjectRepository projectRepository, ProjectService projectService, TeamService teamService, UserService userService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.teamService = teamService;
        this.userService = userService;
    }

    @GetMapping(value = "/categories")
    public String getCategoryPage(Model model, Authentication authentication) {
        return "categories";
    }

    @GetMapping(value = "/projectByCategory")
    public String getProjectByCategory(Model model, Authentication authentication, String category) {
        var projectsByCategory = projectService.getProjectsByCategory(category);

        model.addAttribute("title", "Проекты категории " + category);
        model.addAttribute("category", category);
        model.addAttribute("projects", projectsByCategory);
        System.out.println(projectsByCategory);
        return "categories";
    }

    @PostMapping(value = "/changeProjectCategory")
    public String changeProjectCategory(@RequestParam("projectName") String projectName, @RequestParam("category_projects") String category,
                               Authentication authentication) {
        var project = projectService.getProjectByName(projectName);

        if (project != null) {
            project.setProjectCategory(category);
            projectService.updateProject(project);
        }

        return "redirect:/categories";
    }
}
