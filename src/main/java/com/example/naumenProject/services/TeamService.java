package com.example.naumenProject.services;

import com.example.naumenProject.models.Project;
import com.example.naumenProject.repositories.UserRepository;
import com.example.naumenProject.repositories.TeamRepository;
import com.example.naumenProject.models.ProjectRole;
import com.example.naumenProject.models.User;
import com.example.naumenProject.models.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис, обеспечивающий работу с командами и их членами.
 */
@Slf4j
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    /**
     * Получить команду по её ID.
     *
     * @param id ID команды.
     * @return Объект команды или null, если команда не найдена.
     */
    public Team getTeamById(Long id) {
        log.info("Getting team by ID: {}", id);
        return teamRepository.findById(id).orElse(null);
    }

    public Team getTeamByName(String name) {
        return teamRepository.getTeamByName(name);
    }

    /**
     * Создать новую команду.
     *
     * @param team Объект команды для создания.
     */
    public void createTeam(Team team) {
        log.info("Creating a new team: {}", team);
        teamRepository.save(team);
    }

    public List<Team> getAllTeams() {
        log.info("Getting all teams");
        var iterable = teamRepository.findAll();
        return (List<Team>) iterable;
    }

    /**
     * Обновить информацию о команде.
     *
     * @param team Объект команды для обновления.
     */
    public void updateTeam(Team team) {
        log.info("Updating team: {}", team);
        teamRepository.save(team);
    }

    /**
     * Удалить команду по её ID.
     *
     * @param id ID команды для удаления.
     */
    public void deleteTeam(Long id) {
        log.info("Deleting team by ID: {}", id);
        teamRepository.deleteById(id);
    }

    /**
     * Получить тимлида команды по его ID.
     *
     * @param teamId ID команды.
     * @return Объект студента, являющегося тимлидом команды, или null, если команда не найдена или тимлид не установлен.
     */
    public ProjectRole getTeamLeaderByTeamId(Long teamId) {
        Team team = teamRepository.findById(teamId).orElse(null);
        if (team != null) {
            return team.getTeamLead();
        }
        return null;
    }

    /**
     * Добавить студента в команду и назначить ему роль (не тимлида).
     *
     * @param teamId ID команды.
     * @param lastName Фамилия студента.
     * @param firstName Имя студента.
     * @param role Роль, которую нужно назначить студенту (не тимлида).
     * @return Обновленный объект команды или null, если операция не выполнена.
     */
    public Team addUserToTeamWithRole(Long teamId, String username, String role) {
        Team team = teamRepository.findById(teamId).orElse(null);

        if (team == null || !ProjectRole.TEAM_LEAD.equals(team.getTeamLead())) {
            return null;
        }

        User userToAdd = userRepository.findUserByUsername(username);

        if (userToAdd == null || ProjectRole.TEAM_LEAD.equals(userToAdd.getRoleInProject())) {
            return null;
        }

        // TODO: привести members к типу List<User>
        // team.getMembers().add(userToAdd);
        userToAdd.setRoleInProject(ProjectRole.valueOf(role));

        teamRepository.save(team);
        userRepository.save(userToAdd);

        return team;
    }
}
