package com.ss.app.webservice.service.impl;


import com.ss.app.webservice.service.ParameterService;
import com.ss.app.webservice.repository.custom.ParameterRepositoryCustom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;















@Service
public class ParameterServiceImpl implements ParameterService {

    private static final Logger logger = LoggerFactory.getLogger(ParameterServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    ParameterRepositoryCustom parameterRepository;

    @Override
    public List<Map<String,Object>> findParameterDtlByParameterHdrId(String code){
        logger.info("[Service]findParameterDtlByParameterHdrId :{}",code);
        try{
            return parameterRepository.findParameterDtlByParameterHdrId(code);
        }catch(Exception e){
            logger.error("error msg:{}",e);

            throw new RuntimeException(e);
        }
    }
 
}