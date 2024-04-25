package com.example.naumenProject.repositories;

import com.example.naumenProject.models.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long>
{
    User findUserByLastNameAndFirstName(String lastName, String firstName);
}
