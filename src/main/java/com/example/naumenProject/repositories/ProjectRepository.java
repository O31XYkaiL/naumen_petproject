package com.example.naumenProject.repositories;

import com.example.naumenProject.models.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProjectRepository extends CrudRepository<Project, Long>
{
    List<Project> findByCategory(String category, String subcategory);
}
