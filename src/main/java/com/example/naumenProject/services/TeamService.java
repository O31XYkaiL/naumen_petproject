package com.example.naumenProject.services;

import com.webagregator.webagregator.app.repositories.StudentRepository;
import com.webagregator.webagregator.app.repositories.TeamRepository;
import com.webagregator.webagregator.domain.ProjectRole;
import com.webagregator.webagregator.domain.Student;
import com.webagregator.webagregator.domain.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис, обеспечивающий работу с командами и их членами.
 */
@Slf4j
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, StudentRepository studentRepository) {
        this.teamRepository = teamRepository;
        this.studentRepository = studentRepository;
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

    /**
     * Создать новую команду.
     *
     * @param team Объект команды для создания.
     */
    public void createTeam(Team team) {
        log.info("Creating a new team: {}", team);
        teamRepository.save(team);
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
    public Team addStudentToTeamWithRole(Long teamId, String lastName, String firstName, String role) {
        Team team = teamRepository.findById(teamId).orElse(null);

        if (team == null || !ProjectRole.TEAM_LEAD.equals(team.getTeamLead())) {
            return null;
        }

        Student studentToAdd = studentRepository.findStudentByLastNameAndFirstName(lastName, firstName);

        if (studentToAdd == null || ProjectRole.TEAM_LEAD.equals(studentToAdd.getRoleInProject())) {
            return null;
        }

        team.getTeamMembers().add(studentToAdd);
        studentToAdd.setRoleInProject(ProjectRole.valueOf(role));

        teamRepository.save(team);
        studentRepository.save(studentToAdd);

        return team;
    }
}
