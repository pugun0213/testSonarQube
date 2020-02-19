package com.ss.app.webservice.repository;

import com.ss.app.webservice.domain.application.VoidOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import org.springframework.security.access.prepost.PreAuthorize;

// @PreAuthorize("hasRole('CLIENT')")
// @PreAuthorize("hasAuthority('CLIENT')")
public interface VoidOrderRepository extends JpaSpecificationExecutor<VoidOrder>,JpaRepository<VoidOrder,Long> {
}