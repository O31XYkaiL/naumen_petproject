package com.example.naumenProject.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Objects;

//@Accessors(chain = true)
@Table(name = "block1_team")
@Entity
public class Team
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "members")
    private String members;


    @Column(name = "project_name")
    private String projectName;
    @Column(name = "project_id")
    private String projectId;

    public Team()
    {
    }

    public Team(String teamId, String teamName, String members, String projectName, String projectId) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }



    //    public boolean equals(Object instance)
//    {
//        if (instance == null)
//            return false;
//        if (!(instance instanceof Team))
//            return false;
//        Team other = (Team)instance;
//        if (!(word.getId().equals(other.getWord().getId())))
//            return false;
//        if (!(training.getId().equals(other.getTraining().getId())))
//            return false;
//        if (!(repeatDays == other.getRepeatDays()))
//            return false;
//        if(!(lastStudyDate.equals(other.getLastStudyDate())))
//            return false;
//        return true;
//    }
}
