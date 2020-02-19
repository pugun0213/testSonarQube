package com.ss.app.webservice.repository;

import com.ss.app.webservice.domain.application.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import org.springframework.security.access.prepost.PreAuthorize;

// @PreAuthorize("hasAuthority('CLIENT')")
public interface PaymentDetailRepository extends JpaSpecificationExecutor<PaymentDetail>,JpaRepository<PaymentDetail,Long> {
}