package com.back.repository;

import com.back.model.Ride;
import com.back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Query("SELECT r FROM Ride r WHERE r.user.id = :userId AND r.status = 'COMPLETED'")
    List<Ride> getCompletedRides(@Param("userId") Integer userId);
}
