package com.example.naumenProject.models;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "block1_users")
public class User
{
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_name")
    private String firstName;

    @Column(name = "user_surname")
    private String lastName;


    @Column(name = "team_role")
    private ProjectRole roleInProject;


    public User( String email, String password, String firstName, String lastName, String academicGroup, int voteCount, ProjectRole roleInProject) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleInProject = roleInProject;
    }

    public User()
    {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ProjectRole getRoleInProject() {
        return roleInProject;
    }

    public void setRoleInProject(ProjectRole roleInProject) {
        this.roleInProject = roleInProject;
    }
}
