package com.example.naumenProject.models;


import lombok.Getter;

@Getter
public enum ProjectRole {

    TEAM_LEAD("TeamLead"),
    FRONTEND("Frontend"),
    BACKEND("Backend"),
    DESIGNER("Designer"),
    ANALYST("Analyst"),
    SCIENTIST("Scientist");

    private final String projectRole;

    ProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }
}
