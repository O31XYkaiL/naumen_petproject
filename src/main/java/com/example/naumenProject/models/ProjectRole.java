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

    private final String role;

    ProjectRole(String role) {
        this.role = role;
    }
}
