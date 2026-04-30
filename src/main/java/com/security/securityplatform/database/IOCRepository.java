package com.security.securityplatform.database;

import com.security.securityplatform.model.IOCEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOCRepository extends JpaRepository<IOCEntity, Long> {
    List<IOCEntity> findBySeverityScoreGreaterThan(int score);
    List<IOCEntity> findByType(String type);
    List<IOCEntity> findBySource(String source);
    Optional<IOCEntity> findByValue(String value);
}
