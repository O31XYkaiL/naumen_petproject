package com.example.naumenProject.models;


import lombok.Getter;

@Getter
public enum ProjectRole {

    TEAM_LEAD("TeamLead"),

    BACKEND("Backend"),
    DESIGNER("Designer"),
    SCIENTIST("Scientist");

    private final String role;

    ProjectRole(String role) {
        this.role = role;
    }
}
