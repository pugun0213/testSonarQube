package com.ss.app.webservice.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface MemberTransactionService {

    Map  getPosMemberLogin(String json);
    Map  getMemberProfile(String json);

    Map callFunction(Map<String,Object> data);
    List<Map<String,Object>> findMemberData(); 
    Map<String,Object> findMemberProfileData(); 
    Map<String,Object> updateMemberMobilePhone(String json);
    Map<String,Object> updateMemberProfile(String json);
    
    Map<String,Object> updatePosMemberTransaction(String json);
    Map<String,Object> getMemberHistory(String json);



}