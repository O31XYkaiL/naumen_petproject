package com.example.naumenProject.services;

import com.example.naumenProject.models.ProjectRole;
import com.example.naumenProject.models.Role;
import com.example.naumenProject.models.User;
import com.example.naumenProject.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Service
@Slf4j
public class UserService implements UserDetailsService {
    /**
     * Репозиторий студента
     */
    private final UserRepository userRepository;

    /**
     * Конструктор сервиса.
     *
     * @param userRepository Репозиторий студентов.
     */
    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    /**
     * Получить информацию о студенте по его ID.
     *
     * @param id ID студента.
     * @return Объект студента или null, если студент не найден.
     */
    public User getStudentById(Long id) {
        log.info("Getting student by ID: {}", id);
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Создать нового студента.
     *
     * @param student Объект студента, который будет создан.
     */
    public void createStudent(User student) {
        log.info("Creating a new student: {}", student);
        userRepository.save(student);
    }

    /**
     * Обновить информацию о студенте.
     *
     * @param student Объект студента, который будет обновлен.
     */
    public void updateStudent(User student) {
        log.info("Updating student: {}", student);
        userstudentRepository.save(student);
    }

    /**
     * Удалить студента по его ID.
     *
     * @param id ID студента, который будет удален.
     */
    public void deleteStudent(Long id) {
        log.info("Deleting student by ID: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String firstName, String lastName) throws UsernameNotFoundException {
        User myUser = userRepository.findStudentByLastNameAndFirstName(firstName, lastName);
        return new org.springframework.security.core.userdetails.User(myUser.getFirstName(), myUser.getPassword(),
                mapRolesToAthorities(myUser.getRole()));
    }

    private List<? extends GrantedAuthority> mapRolesToAthorities(Set<Role> roles) {
        return roles.stream().map(r -> (new SimpleGrantedAuthority("ROLE_" + r.name())).toList());
    }

    public String addUser(User user) throws Exception{
        User userFromDb = userRepository.findStudentByLastNameAndFirstName(user.getFirstName(), user.getLastName());
        if(userFromDb != null){
            throw new Exception("user exist");
        }
        user.setRole(Collections.singleton(Role.USER));
        user.setActive(true);
        userRepository.save(user);
        return "redirect:/login";

    }
}
