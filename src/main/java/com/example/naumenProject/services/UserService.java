package com.example.naumenProject.services;

import com.example.naumenProject.models.User;
import com.example.naumenProject.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    /**
     * Репозиторий студента
     */
    private final UserRepository studentRepository;

    /**
     * Конструктор сервиса.
     *
     * @param studentRepository Репозиторий студентов.
     */
    @Autowired
    public UserService(UserRepository studentRepository)
    {
        this.studentRepository = studentRepository;
    }

    /**
     * Получить информацию о студенте по его ID.
     *
     * @param id ID студента.
     * @return Объект студента или null, если студент не найден.
     */
    public User getStudentById(Long id) {
        log.info("Getting student by ID: {}", id);
        return studentRepository.findById(id).orElse(null);
    }

    /**
     * Создать нового студента.
     *
     * @param student Объект студента, который будет создан.
     */
    public void createStudent(User student) {
        log.info("Creating a new student: {}", student);
        studentRepository.save(student);
    }

    /**
     * Обновить информацию о студенте.
     *
     * @param student Объект студента, который будет обновлен.
     */
    public void updateStudent(User student) {
        log.info("Updating student: {}", student);
        studentRepository.save(student);
    }

    /**
     * Удалить студента по его ID.
     *
     * @param id ID студента, который будет удален.
     */
    public void deleteStudent(Long id) {
        log.info("Deleting student by ID: {}", id);
        studentRepository.deleteById(id);
    }

}
