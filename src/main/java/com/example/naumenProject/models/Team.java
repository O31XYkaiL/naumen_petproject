package com.example.naumenProject.models;

import jakarta.persistence.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "block1_team")
public class Team
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "members")
    private String members;

    @Column(name = "projects")
    private String projectName;

    public Team()
    {
    }

    public Team(Long id, String teamName, String members, String projectName) {
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

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
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
