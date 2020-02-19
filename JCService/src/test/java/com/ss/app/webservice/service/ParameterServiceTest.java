package com.ss.app.webservice.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.junit.Assert;
import com.ss.app.webservice.service.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
public class ParameterServiceTest  extends AbstractApplicationTests{
    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterServiceTest.class);

    @Autowired
    ParameterService parameterService;

    @Test 
    public void testFindParameter_1(){
      LOGGER.info("- testfindParameterDtlByParameterHdrId -");
      try{
          List<Map<String,Object>> listResults = new ArrayList<>();
          listResults = parameterService.findParameterDtlByParameterHdrId("198");
          for(Map<String,Object> result : listResults){
            LOGGER.debug(" code :{}  SEQ:{}  PARAMETER_ID:{} VALUE_1:{} VALUE_2:{} ",result.get("code"),result.get("paramSeq"),result.get("paramHdr"),result.get("paramterValue1"),result.get("paramterValue2") );
          }
         Assert.assertTrue(listResults.size() == 6 );

      }catch(Exception e){
          LOGGER.error("error msg:{}",e);
          e.printStackTrace();
          throw new RuntimeException(e);
      }

    }
}
