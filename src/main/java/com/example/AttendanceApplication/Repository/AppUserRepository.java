package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.AppUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    @Query(value = """
            SELECT u FROM AppUser u 
            WHERE u.email = :email
                AND u.delFlag = false 
            """)
    Optional<AppUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE " +
            "WHERE a.email = ?1")
    int enableAppUser(String email);
}
