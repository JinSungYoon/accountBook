package core.security.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import core.security.domain.entity.MemberEntity;


public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	Optional<MemberEntity> findByEmail(String userEmail);
}
