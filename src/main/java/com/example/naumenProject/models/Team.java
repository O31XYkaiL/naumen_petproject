package com.example.naumenProject.models;

import jakarta.persistence.*;
import lombok.experimental.Accessors;

import java.util.*;

@Entity
@Table(name = "block1_team")
public class Team
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "project_name")
    private String projectName;

    @ManyToMany
    @JoinTable(
            name = "team_project",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "team_user",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    public Team()
    {
    }

    public Team(Long id, String teamName, Set<User> members, String projectName) {
        this.id = id;
        this.teamName = teamName;
        this.members = members;
        this.projectName = projectName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Set<User> getMembers() {
        return members;
    }

    public Set<String> getMembersNames() {
        Set<String> membersNames = new HashSet<>();
        for (User user : members) {
            membersNames.add(user.getUsername());
        }
        return membersNames;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        members.remove(user);
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ProjectRole getTeamLead() {
        // TODO: добавить тимлида в класс команды
        // TODO: тимлид по идее должен вернуть User, не Project Role?
        return null;
    }

}
