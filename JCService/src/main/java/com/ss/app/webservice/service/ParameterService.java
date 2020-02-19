package com.ss.app.webservice.service;

import java.util.List;
import java.util.Map;

public interface ParameterService {
	List<Map<String,Object>> findParameterDtlByParameterHdrId(String code);
}

