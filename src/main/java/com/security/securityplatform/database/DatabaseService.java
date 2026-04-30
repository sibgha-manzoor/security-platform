package com.security.securityplatform.database;

import com.security.securityplatform.model.IOCEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    private IOCRepository iocRepository;

    public IOCEntity saveIOC(IOCEntity entity) {
        // Check for duplicate before saving
        if (iocRepository.findByValue(entity.getValue()).isPresent()) {
            System.out.println("Duplicate IOC skipped: " + entity.getValue());
            return null;
        }
        return iocRepository.save(entity);
    }

    public List<IOCEntity> getAllIOCs() {
        return iocRepository.findAll();
    }

    public List<IOCEntity> getHighRiskIOCs() {
        return iocRepository.findBySeverityScoreGreaterThan(75);
    }

    public List<IOCEntity> getIOCsByType(String type) {
        return iocRepository.findByType(type);
    }

    public List<IOCEntity> getIOCsBySource(String source) {
        return iocRepository.findBySource(source);
    }

    public long getTotalCount() {
        return iocRepository.count();
    }
}
