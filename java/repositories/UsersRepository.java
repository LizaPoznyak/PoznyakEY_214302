package com.incorpr.repositories;

import com.incorpr.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
