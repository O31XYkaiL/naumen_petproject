package com.example.naumenProject.models;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "block1_project")
public class Project
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "description")
    private String projectDescription;

    @Column(name = "video_gameplay")
    private String gameplayVideo;

    @Column(name = "cover")
    private String coverImage;

    @Column(name = "category_projects")
    private String projectCategory;

    @Column(name = "github_link")
    private String repositoryLink;

    @Column(name = "project_archive")
    private String projectArchivePath;


    public Project()
    {
    }

    public Project(Long projectId, String projectName, String projectDescription, String gameplayVideo, String coverImage, String projectCategory, String repositoryLink, String projectArchivePath) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.gameplayVideo = gameplayVideo;
        this.coverImage = coverImage;
        this.projectCategory = projectCategory;
        this.repositoryLink = repositoryLink;
        this.projectArchivePath = projectArchivePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getGameplayVideo() {
        return gameplayVideo;
    }

    public void setGameplayVideo(String gameplayVideo) {
        this.gameplayVideo = gameplayVideo;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }

    public String getRepositoryLink() {
        return repositoryLink;
    }

    public void setRepositoryLink(String repositoryLink) {
        this.repositoryLink = repositoryLink;
    }

    public String getProjectArchivePath() {
        return projectArchivePath;
    }

    public void setProjectArchivePath(String projectArchivePath) {
        this.projectArchivePath = projectArchivePath;
    }
}
