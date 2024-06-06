package com.example.naumenProject.repositories;

import com.example.naumenProject.models.ProjectRole;
import com.example.naumenProject.models.User;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long>
{
    User findUserById(Long id);
    User findUserByEmail(String email);
    User findUserByUsername(String username);
    User getUserByUsername(String username);
}
