package com.assingment.milestone1.DatabaseFiles;

import com.assingment.milestone1.Dataclass.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
