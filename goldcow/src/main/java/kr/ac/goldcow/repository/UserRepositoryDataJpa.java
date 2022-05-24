package kr.ac.goldcow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import kr.ac.goldcow.model.User;

public interface UserRepositoryDataJpa extends JpaRepository<User, Long>{
List<User> getUsersByEmail(@Param("email") String email);
List<User> getUsersByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}
