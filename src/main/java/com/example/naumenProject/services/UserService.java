package com.example.naumenProject.services;

import com.example.naumenProject.models.Project;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
@Service
@Slf4j
public class UserService implements UserDetailsService {
    /**
     * Репозиторий пользователя.
     */
    private final UserRepository userRepository;

    /**
     * Конструктор сервиса.
     *
     * @param userRepository Репозиторий пользователей.
     */
    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        log.info("Getting user by ID: {}", id);
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String name) {
        return userRepository.getUserByUsername(name);
    }

    /**
     * Создать нового пользователя.
     *
     * @param user Объект пользователя, который будет создан.
     */
    public void createUser(User user) {
        log.info("Creating a new user: {}", user);
        userRepository.save(user);
    }

    /**
     * Обновить информацию о студенте.
     *
     * @param user Объект студента, который будет обновлен.
     */
    public void updateUser(User user) {
        log.info("Updating user: {}", user);
        userRepository.save(user);
    }

    /**
     * Удалить студента по его ID.
     *
     * @param id ID студента, который будет удален.
     */
    public void deleteUser(Long id) {
        log.info("Deleting user by ID: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // public UserDetails loadUserByUsername(String firstName, String lastName) throws UsernameNotFoundException {
        User myUser = userRepository.findUserByUsername(username);
        return new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(),
                mapRolesToAthorities(myUser.getRole()));
    }

    private List<? extends GrantedAuthority> mapRolesToAthorities(Set<Role> roles) {
        return roles.stream().map(r -> (new SimpleGrantedAuthority("ROLE_" + r.name()))).toList();
    }

    public String addUser(User user) throws Exception{
        User userFromDb = userRepository.findUserByUsername(user.getUsername());
        if(userFromDb != null){
            throw new Exception("user exist");
        }
        user.setRole(Collections.singleton(Role.USER));
        user.setActive(true);
        userRepository.save(user);
        return "redirect:/login";

    }
}
