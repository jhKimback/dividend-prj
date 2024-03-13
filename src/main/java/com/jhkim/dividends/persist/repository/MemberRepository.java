package com.jhkim.dividends.persist.repository;

import com.jhkim.dividends.persist.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
  Optional<MemberEntity> findByUsername(String username);

  boolean existsByUsername(String username);
}
