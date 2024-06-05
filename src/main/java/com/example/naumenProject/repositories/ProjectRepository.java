package com.example.naumenProject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.naumenProject.models.Project;


public interface ProjectRepository extends JpaRepository<Project, Long>
{
    @Query("SELECT p FROM Project p WHERE " +
           "LOWER(p.projectName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.projectDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.projectCreator) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Project> searchProjects(@Param("query") String query);

    @Query("SELECT p FROM Project p WHERE p.projectCreator = :username")
    List<Project> getProjectsByUser(@Param("username") String username);

    @Query("SELECT p FROM Project p ORDER BY p.projectRating DESC")
    List<Project> getProjectsSortedByRating();

    @Query("SELECT p FROM Project p WHERE p.projectName = :name")
    Project getProjectByName(@Param("name") String name);

//    @Query("SELECT p FROM Project p WHERE p.projectCategory = :category_projects")
//    List<Project> findProjectsByProjectCategory(@Param("category_projects") String category);

    @Query("SELECT p FROM Project p WHERE p.projectCategory = :category_projects")
    List<Project> getProjectsByProjectCategory(@Param("category_projects") String category);
}
