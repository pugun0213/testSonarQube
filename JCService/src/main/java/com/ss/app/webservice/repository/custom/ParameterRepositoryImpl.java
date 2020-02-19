package com.ss.app.webservice.repository.custom;



import java.util.*;
import org.springframework.stereotype.Service;





import javax.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.json.*;






import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;














@Service
public class ParameterRepositoryImpl implements ParameterRepositoryCustom {
	
    private static Logger logger = LoggerFactory.getLogger(ParameterRepositoryImpl.class);
	private static final String posTrackNumber1= "posTrackNumber";


	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Map<String,Object>> findParameterDtlByParameterHdrId(String code){
		logger.info("[Repository]findParameterDtlByParameterHdrCode : {}",code);
		try{

			StringBuilder sqlStatement = new StringBuilder();
			sqlStatement.append(" select padcha1,   padcha2,   padcha3  \n");
			sqlStatement.append(" from su_param_dtl  \n");
			sqlStatement.append(" where padparam_id   = 100  \n");
			sqlStatement.append(" and padentry_code = :code  \n");
			
			Query query = em.createNativeQuery(sqlStatement.toString());
			query.setParameter("code",code);
			List<Object[]> rows = null;
			List<Map<String,Object>> result = new ArrayList<>();
			rows = query.getResultList();
			for(Object[] col :  rows){
				Map<String,Object> mapResult = new HashMap();
				mapResult.put("user",col[0]);
				mapResult.put("password",col[1]);
				mapResult.put("role",col[2]);
				result.add(mapResult);
			}
			logger.debug(" get Result size : {} ",result.size());
			return result;
		}catch(Exception e){
			 logger.error("error msg1:{}",e);

             throw new RuntimeException(e);
		}
	}

	@Override
	public List<Map<String,Object>> findOrderHdrByPOStrackNumber(String posTrackNumber){
		try{
			StringBuilder sqlStatement = new StringBuilder();
			sqlStatement.append("SELECT POS_TRACK_NUMBER , COM_CODE FROM ws_order_hdr WHERE  POS_TRACK_NUMBER = :posTrackNumber  \n");
			Query query = em.createNativeQuery(sqlStatement.toString());
			query.setParameter(posTrackNumber1,posTrackNumber);
			List<Object[]> rows = null;
			List<Map<String,Object>> result = new ArrayList<>();
			rows = query.getResultList();
			for(Object[] col :  rows){
				Map<String,Object> mapResult = new HashMap();
				mapResult.put(posTrackNumber1,col[0]);
				mapResult.put("comCode",col[1]);
				result.add(mapResult);
			}
			return result;
		}catch(Exception e){
			 logger.error("error msg2:{}",e);

             throw new RuntimeException(e);
		}
	}

	@Override
	public List<Map<String,Object>> findPaymentHdrByPOStrackNumber(String posTrackNumber){
		try{
			StringBuilder sqlStatement = new StringBuilder();

			sqlStatement.append("SELECT PRE_ORDER_NUMBER , COM_CODE FROM ws_payment_hdr WHERE  PRE_ORDER_NUMBER = :posTrackNumber  \n");
			Query query = em.createNativeQuery(sqlStatement.toString());
			query.setParameter(posTrackNumber1,posTrackNumber);
			List<Object[]> rows = null;
			List<Map<String,Object>> result = new ArrayList<>();
			rows = query.getResultList();
			for(Object[] col :  rows){
				Map<String,Object> mapResult = new HashMap();
				mapResult.put(posTrackNumber1,col[0]);
				mapResult.put("comCode",col[1]);
				result.add(mapResult);
			}
			return result;
		}catch(Exception e){
			 logger.error("error msg3:{}",e);

             throw new RuntimeException(e);
		}
	}
   

}
