package com.ss.app.webservice.repository.custom;


import java.util.*;
import org.springframework.stereotype.Service;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.json.*;




import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;














@Service
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {
	
    private static Logger logger = LoggerFactory.getLogger(CustomerRepositoryImpl.class);








	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Map<String,Object> getEnjoyCardNumberByTelNoByIdCard(String telNo,String idCard){
		try{
            logger.info("[01] getEnjoyCardNumberByTelNoByIdCard  ======== :{} :{}",telNo,idCard);
			return null;
		}catch(Exception e){

            logger.error("msg1:{}",e.getMessage());
            throw new RuntimeException(e);    
		}
	}
   @Override
	public Map<String,Object> getProfileByEnjoyCardNumber(String enjoyCardNumber){
		try{
            logger.info("[01] getProfileByEnjoyCardNumber  ======== :{} ",enjoyCardNumber);
			return null;
		}catch(Exception e){

            logger.error("msg2:{}",e.getMessage());
            throw new RuntimeException(e);    
		}
	}

	@Override
	public Map<String,Object> getPointBalance(String enjoyCardNumber){
		try{
            logger.info("[01] getPointBalance  ======== :{} ",enjoyCardNumber);
			return null;
		}catch(Exception e){

            logger.error("msg3:{}",e.getMessage());
            throw new RuntimeException(e);    
		}
	}

}
