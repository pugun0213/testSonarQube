package com.ss.app.webservice.repository.custom;

import java.util.List;
import java.util.Map;


public interface ParameterRepositoryCustom  {

	List<Map<String,Object>> findParameterDtlByParameterHdrId(String code);	
	List<Map<String,Object>> findOrderHdrByPOStrackNumber(String posTrackNumber);	
	List<Map<String,Object>> findPaymentHdrByPOStrackNumber(String posTrackNumber);	

}
	