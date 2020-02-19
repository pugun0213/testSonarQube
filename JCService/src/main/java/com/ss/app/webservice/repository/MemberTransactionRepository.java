package com.ss.app.webservice.repository;

import com.ss.app.webservice.domain.application.MemberTransaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberTransactionRepository extends JpaRepository<MemberTransaction,Long> {

}