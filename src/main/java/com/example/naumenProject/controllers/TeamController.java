package com.example.naumenProject.controllers;

import com.example.naumenProject.models.ProjectRole;
import com.example.naumenProject.models.Team;
import com.example.naumenProject.models.User;
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

import java.util.*;

@Controller
public class TeamController {
    private final UserRepository userRepository;
    public final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final TeamService teamService;

    private final UserService userService;

    @Autowired
    public TeamController(UserRepository userRepository, ProjectRepository projectRepository, ProjectService projectService, TeamService teamService, UserService userService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.teamService = teamService;
        this.userService = userService;
    }

    @GetMapping(value = "/teams")
    public String getTeamPage(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        var teams = teamService.getAllTeams();

        model.addAttribute("title", "Команды");
        model.addAttribute("user", currentUser);
        model.addAttribute("teams", teams);

        return "teams";
    }

    @PostMapping(value = "/createTeam")
    public String createTeam(@RequestParam("team_name") String teamName,
                             Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        Set<User> members = new HashSet<>();
        members.add(currentUser);

        Team team = new Team(UUID.randomUUID().getMostSignificantBits(), teamName, members, "");

        teamService.createTeam(team);

        return "redirect:/teams";
    }

    @PostMapping(value = "/joinToTeam")
    public String joinToTeam(@RequestParam("team_name") String teamName,
                             Authentication authentication) {
        var team = teamService.getTeamByName(teamName);

        String username = authentication.getName();
        User currentUser = userRepository.findUserByUsername(username);

        if (team != null) {
            team.addMember(currentUser);
            teamService.updateTeam(team);
        }

        return "redirect:/teams";
    }

    @PostMapping(value = "/leaveTeam")
    public String leaveTeam(@RequestParam("team_name") String teamName,
                            Authentication authentication) {
        var team = teamService.getTeamByName(teamName);

        String username = authentication.getName();
        User currentUser = userRepository.findUserByUsername(username);

        if (team != null) {
            team.removeMember(currentUser);
            teamService.updateTeam(team);
        }

        return "redirect:/teams";
    }

    @PostMapping(value = "/chooseTeamRole")
    public String chooseTeamRole(@RequestParam("name") String roleInProject,
                                 Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        currentUser.setRoleInProject(roleInProject);

        userService.updateUser(currentUser);

        return "redirect:/";
    }

    @PostMapping(value = "/addUserToTeam")
    public String addUserToTeam(@RequestParam("team_name") String teamName,
                                @RequestParam("username") String username,
                                Authentication authentication) {
        var team = teamService.getTeamByName(teamName);
        var user = userRepository.findUserByUsername(username);

        if (team != null && user != null) {
            team.addMember(user);
            teamService.updateTeam(team);
        }

        return "redirect:/teams";
    }
}
