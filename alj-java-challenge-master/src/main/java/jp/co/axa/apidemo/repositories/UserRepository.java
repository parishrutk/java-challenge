package jp.co.axa.apidemo.repositories;

import jp.co.axa.apidemo.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {

    Optional<UserInfo> findByUserName(String userName);
}
