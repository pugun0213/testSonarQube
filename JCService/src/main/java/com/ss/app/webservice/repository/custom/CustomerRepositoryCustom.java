package com.ss.app.webservice.repository.custom;

import java.util.Map;


public interface CustomerRepositoryCustom  {
	Map<String,Object> getEnjoyCardNumberByTelNoByIdCard(String telNo,String idCard);
	Map<String,Object> getProfileByEnjoyCardNumber(String enjoyCard);	
	Map<String,Object> getPointBalance(String enjoyCard);	
	
}
	