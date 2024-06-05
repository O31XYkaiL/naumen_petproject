package com.example.naumenProject.models;


import java.io.File;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "block1_project")
public class Project
{
    @Id
    @GeneratedValue
    private Long id;

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

    @Column(name = "project_rating")
    private Integer projectRating;

    @Column(name = "project_creator")
    private String projectCreator;


    public Project()
    {
    }

    public Project(Long id, String projectCreator, String projectName, String projectDescription, String gameplayVideo, String coverImage, String projectCategory, String repositoryLink, String projectArchivePath) {
        this.id = id;
        this.projectCreator = projectCreator;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.gameplayVideo = gameplayVideo;
        this.coverImage = coverImage;
        this.projectCategory = projectCategory;
        this.repositoryLink = repositoryLink;
        this.projectArchivePath = projectArchivePath;
        this.projectRating = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectCreator() {
        return projectCreator;
    }

    public void setProjectCreator(String projectCreator) {
        this.projectCreator = projectCreator;
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

    public User getCreator() {
        return null;
    }

    public void setCreator(User creator) {
        // this.creator = creator;
    }

    public Integer getProjectRating() {
        return projectRating;
    }

    public void setProjectRating(Integer projectRating) {
        this.projectRating = projectRating;
    }

    public void addProjectRating() {
        this.projectRating += 1;
    }

    public String getUploadDir() {
        return System.getProperty("user.dir") + File.separator + "uploads" + File.separator + String.valueOf(getId()) + File.separator;
    }
}
