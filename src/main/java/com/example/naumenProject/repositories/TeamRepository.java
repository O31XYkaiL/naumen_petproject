package com.example.naumenProject.repositories;

import com.example.naumenProject.models.Project;
import com.example.naumenProject.models.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends CrudRepository<Team, Long>
{
    @Query("SELECT p FROM Team p WHERE p.teamName = :name")
    Team getTeamByName(@Param("name") String name);
}
