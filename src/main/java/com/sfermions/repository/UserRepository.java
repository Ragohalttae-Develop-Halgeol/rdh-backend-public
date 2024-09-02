package com.sfermions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sfermions.entity.User;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
}
//이메일로 유저 정보 조회 메서드 추가