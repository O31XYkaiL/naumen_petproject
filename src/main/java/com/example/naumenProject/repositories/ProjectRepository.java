package com.example.naumenProject.repositories;

import com.example.naumenProject.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ProjectRepository extends JpaRepository<Project, Long>
{
    @Query("SELECT p FROM Project p WHERE " +
           "LOWER(p.projectName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.projectDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.projectCreator) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Project> searchProjects(@Param("query") String query);

    @Query("SELECT p FROM Project p WHERE p.projectCreator = :username")
    List<Project> getProjectsByUser(@Param("username") String username);
}
