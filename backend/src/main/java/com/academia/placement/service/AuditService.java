package com.academia.placement.service;

import com.academia.placement.model.AuditLog;
import com.academia.placement.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordEvent(UUID actorId, String actorRole, String actionType,
                            String entityName, UUID entityId, String details, String ipAddress) {
        AuditLog log = new AuditLog(actorId, actorRole, actionType, entityName, entityId, details, ipAddress);
        auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogs(int page, int size) {
        return auditLogRepository.findAllByOrderByRecordedAtDesc(PageRequest.of(page, size));
    }
}

