package com.ss.app.webservice.service.impl;

import com.ss.app.webservice.service.MemberTransactionService;
import com.ss.app.webservice.repository.MemberTransactionRepository;


import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;


import javax.persistence.*;


import org.json.JSONObject;
import flexjson.JSONDeserializer;


import org.json.*;


import javax.persistence.EntityManager;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class MemberTransactionServiceImpl implements MemberTransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberTransactionServiceImpl.class);
    private SimpleDateFormat DATE_FORMAT_PARAM = new SimpleDateFormat("dd/MM/yyyy");


    @Autowired
    MemberTransactionRepository memberTransactionRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Map getPosMemberLogin(String json){
    	try{
            List<Map<String,Object>> data = new ArrayList<>();
    		Map returnResult = new HashMap<>();
            Map result = null;
    		LOGGER.info("[01]getPosMemberLogin ");
    		JSONObject jsonObject = new JSONObject(json);
            Map<String,Object> memberCriteria = new JSONDeserializer<Map<String, Object>>().deserialize(jsonObject.toString());
            Map mapforCallData = new HashMap<>();
            mapforCallData.put("function","get_member_login");
            mapforCallData.put("comCode",memberCriteria.get("comCode") ==null?null:memberCriteria.get("comCode").toString() );
            mapforCallData.put("idCard",memberCriteria.get("idCard") ==null?null:memberCriteria.get("idCard").toString());
            mapforCallData.put("telephoneNum",memberCriteria.get("telephoneNum") ==null?null:memberCriteria.get("telephoneNum").toString());
            mapforCallData.put("memberType",memberCriteria.get("memberType") ==null?null:memberCriteria.get("memberType").toString());
            
            LOGGER.debug("[02]getPosMemberLogin  :[{}]",mapforCallData);	
            result = callFunction(mapforCallData);
            if(  Integer.valueOf( result.get("size").toString()) > 0  ){
                data = findMemberData();
                returnResult.put("responseCode","01");
                returnResult.put("responseMsg",null);
                returnResult.put("cardList",data);

            }else{
              returnResult.put("responseCode","02");
              returnResult.put("responseMsg",result.get("errorMsg"));
              returnResult.put("cardList",data);

            }

            // ou = JAY, tel = 0898907889, idcard = 3720101061801
    		return returnResult;
    	}catch(Exception e){
           LOGGER.error("error msg :{}",e);
            throw new RuntimeException(e.getMessage());
    	}
    }
    
    @Override
    public Map getMemberProfile(String json){
        try{
            Map<String,Object> data = new HashMap<>();
            Map returnResult = new HashMap<>();
            
            JSONObject jsonObject = new JSONObject(json);
            Map<String,Object> memberCriteria = new JSONDeserializer<Map<String, Object>>().deserialize(jsonObject.toString());
             Map mapforCallData = new HashMap<>();
            mapforCallData.put("function","get_member_profile");
            mapforCallData.put("comCode",memberCriteria.get("comCode") ==null?null:memberCriteria.get("comCode").toString() );
            mapforCallData.put("memberClub",memberCriteria.get("memberClub") ==null?null:memberCriteria.get("memberClub").toString() );
            mapforCallData.put("memberCard",memberCriteria.get("memberCard") ==null?null:memberCriteria.get("memberCard").toString() );
            mapforCallData.put("memberType",memberCriteria.get("memberType") ==null?null:memberCriteria.get("memberType").toString());
            LOGGER.debug("[02]getMemberProfile  :[{}]",mapforCallData);    
            Map result = null;
            result = callFunction(mapforCallData);
            if(  Integer.valueOf( result.get("size").toString() ) > 0  ){
              data = findMemberProfileData();
              data.put("pointDate",DATE_FORMAT_PARAM.format(result.get("pointDate")));
              data.put("pointBalance",result.get("pointBalance"));
              data.put("pointExpire",result.get("pointExpire"));
              data.put("expireDate",DATE_FORMAT_PARAM.format(result.get("expireDate")));
              
              if(data.get("memberCardId") == null ){
                returnResult.put("responseCode","02");
                returnResult.put("responseMsg","not found member data!");
              }else{
                returnResult.put("responseCode","01");
                returnResult.put("responseMsg","");
              }
                returnResult.put("member",data);
              


            }else{
              returnResult.put("responseCode","02");
              returnResult.put("responseMsg",result.get("errorMsg"));
              returnResult.put("member",data);

            }         



            return returnResult;
        }catch(Exception e){
           LOGGER.error("error msg :{}",e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<String,Object> findMemberProfileData(){
      try{
          Map<String,Object> result = new HashMap<>();
          List<  Map<String,Object>> listWarrantly = new ArrayList<>();
          LOGGER.info("findMemberProfileData ");
          StringBuilder sqlStatement  = new StringBuilder();
          sqlStatement.append("SELECT  PRODUCTCODE , PRODUCTNAME , CLUB_CODE , MEMBERCARD_ID , CUST_PHONE , FIRST_NAME , LAST_NAME ");
          sqlStatement.append(" , CEN_ADDRESS,CEN_TUMBON , CEN_AMPHUR , CEN_PROVINCE , CEN_POSTAL  ");
          sqlStatement.append(" , SERIALNUMBER , TO_CHAR(PURCHASEDATE,'DD/MM/YYYY') , PURCHASEINVOICE  ");
          sqlStatement.append(" , WARRANTYTYPE , SWAPCONDITION , TO_CHAR(WARRANTYSTARTDATE,'DD/MM/YYYY') , TO_CHAR(WARRANTYEXPIRED,'DD/MM/YYYY')  , email , TO_CHAR(DATE_OF_BIRTH,'DD/MM/YYYY')  ");
          
          sqlStatement.append(" FROM  v$ws_profile_member ");
          Query query = entityManager.createNativeQuery(sqlStatement.toString());
          List<Object[]> rows = null;
          rows = query.getResultList();
          for(Object[] col :rows){
            Map<String,Object> itemWarrantly = new HashMap<>();
            
            result.put("memberClub",col[2]);
            result.put("memberCardId",col[3]);
            result.put("telephoneNumber",col[4]);
            result.put("email",col[19]);
            result.put("firstName",col[5]);
            result.put("lastName",col[6]);
            result.put("address",col[7]);
            result.put("subDistrictCode",col[8]);
            result.put("districtCode",col[9]);
            result.put("provinceCode",col[10]);
            result.put("postal",col[11]);
            result.put("dateOfBirth",col[20]);

            itemWarrantly.put("productCode",col[0]);
            itemWarrantly.put("productName",col[1]);
            itemWarrantly.put("serialNumber",col[12]);
            itemWarrantly.put("purchaseDate",col[13]);
            itemWarrantly.put("purchaseInvoice",col[14]);
            itemWarrantly.put("warrantyType",col[15]);
            itemWarrantly.put("swapCondition",col[16]);
            itemWarrantly.put("warrantyStartDate",col[17]);
            itemWarrantly.put("warrantyExprired",col[18]);
            listWarrantly.add(itemWarrantly);
          }
          result.put("warrantyList",listWarrantly);
         LOGGER.debug("getfindMemberProfileData result :{}",result);
        return result;
      }catch(Exception e){
        LOGGER.error("error msg :{}",e);
        throw new RuntimeException(e.getMessage());
      }
    }

    @Override
    public  List<Map<String,Object>> findMemberData(){
        try{
            LOGGER.info("findMemberData ");
            StringBuilder sqlStatement  = new StringBuilder();
            sqlStatement.append("SELECT CLUB_CODE,MEMBERCARD_ID FROM  v$ws_invoice_member ");
            Query query = entityManager.createNativeQuery(sqlStatement.toString());
            List<Object[]> rows = null;
            List<Map<String,Object>> result = new ArrayList<>();
            rows = query.getResultList();
            for(Object[] col :rows){
                Map<String,Object> mapResult = new HashMap();
                mapResult.put("memberClub",col[0]);
                mapResult.put("memberCardId",col[1]);
                result.add(mapResult);

            }
            LOGGER.info("[02]findMemberData return :{}",result.size());

            return result;
        }catch(Exception e){
            LOGGER.error("error msg :{}",e);
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public Map callFunction(Map<String,Object> data){
        try{
            Session session = entityManager.unwrap(Session.class);
             Map result = null;
              result = session.doReturningWork(new ReturningWork<Map>() {
                @Override
                public Map execute(Connection connection) throws SQLException {
                    CallableStatement cStmt  = null;
                    Map returnResult = new HashMap<>();
                    try {
                    LOGGER.debug("data : {}",data);
                    if( "get_member_login".equalsIgnoreCase(data.get("function").toString())  ){
                        cStmt = connection.prepareCall("{call WS_JMB_API.get_member_login(?,?,?,?,?,?) }");
                       // varchar 1 ,2,3,5 interger  4
                          cStmt.setString(1, data.get("comCode").toString()  );
                          cStmt.setString(2, data.get("telephoneNum").toString()  );
                          cStmt.setString(3, data.get("idCard").toString()  );
                          cStmt.setString(4, data.get("memberType").toString()  );
                        
                          cStmt.registerOutParameter(5, java.sql.Types.NUMERIC );
                          cStmt.registerOutParameter(6, java.sql.Types.VARCHAR );
                          cStmt.executeUpdate();
                          Integer size = null;
                          String errorMsg = "";
                          size = cStmt.getInt(5);
                          errorMsg = cStmt.getString(6);
                          returnResult.put("size",size);
                          returnResult.put("errorMsg",errorMsg);
                    }else if("get_member_profile".equalsIgnoreCase(data.get("function").toString())){
                          cStmt = connection.prepareCall("{call WS_JMB_API.GET_MEMBER_PROFILE(?,?,?,?,?,?,?,?,?,?) }");
                          cStmt.setString(1, data.get("comCode").toString()  );
                          cStmt.setString(2, data.get("memberClub").toString()  );
                          cStmt.setString(3, data.get("memberCard").toString()  );
                          cStmt.setString(4, data.get("memberType").toString()  );
                          cStmt.registerOutParameter(5, java.sql.Types.NUMERIC );
                          cStmt.registerOutParameter(6, java.sql.Types.VARCHAR );
                          cStmt.registerOutParameter(7, java.sql.Types.DATE );
                          cStmt.registerOutParameter(8, java.sql.Types.NUMERIC );
                          cStmt.registerOutParameter(9, java.sql.Types.NUMERIC );
                          cStmt.registerOutParameter(10, java.sql.Types.DATE );
                          cStmt.executeUpdate();
                          Integer size = null;
                          String errorMsg = "";
                          Date  pointDate  = null;   
                          Integer pointBalance = null;
                          Integer pointExpire = null;
                          Date expireDate = null ;
                          size = cStmt.getInt(5);
                          errorMsg = cStmt.getString(6);
                          pointDate = cStmt.getDate(7);
                          pointBalance = cStmt.getInt(8);
                          pointExpire = cStmt.getInt(9);
                          expireDate = cStmt.getDate(10);

                          returnResult.put("size",size);
                          returnResult.put("errorMsg",errorMsg);
                          returnResult.put("pointDate",pointDate);
                          returnResult.put("pointBalance",pointBalance);
                          returnResult.put("pointExpire",pointExpire);
                          returnResult.put("expireDate",expireDate);

                    }else if("update_member_mobile".equalsIgnoreCase(data.get("function").toString()) ){
                          cStmt = connection.prepareCall("{call WS_JMB_API.UPDATE_MEMBER_MOBILE(?,?,?,?,?,?,?,?) }");
                          cStmt.setString(1, data.get("comCode").toString()  );
                          cStmt.setString(2, data.get("memberClub").toString()  );
                          cStmt.setString(3, data.get("memberCardId").toString()  );
                          cStmt.setString(4, data.get("updateMobileNumber").toString()  );
                          cStmt.setString(5, data.get("memberType").toString()  );
                          cStmt.setString(6, data.get("updatedBy").toString()  );

                          cStmt.registerOutParameter(7, java.sql.Types.NUMERIC );
                          cStmt.registerOutParameter(8, java.sql.Types.VARCHAR );
                          cStmt.executeUpdate();
                          Integer size = null;
                          String errorMsg = "";
                          size = cStmt.getInt(7);
                          errorMsg = cStmt.getString(8);
                          if(size == 0 ){
                            returnResult.put("responseCode","02");
                          }else{
                            returnResult.put("responseCode","01");
                          }
                          returnResult.put("size",size);
                          returnResult.put("errorMsg",errorMsg);
                         

                    }else if("update_member_profile".equalsIgnoreCase(data.get("function").toString()) ){
                          cStmt = connection.prepareCall("{call WS_JMB_API.UPDATE_MEMBER_PROFILE(?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
                          cStmt.setString(1, data.get("comCode").toString()  );
                          cStmt.setString(2, data.get("memberClub").toString()  );
                          cStmt.setString(3, data.get("memberCardId").toString()  );
                          cStmt.setString(4, data.get("emailAddress").toString()  );
                          cStmt.setString(5, data.get("detailAddress").toString()  );
                          cStmt.setString(6, data.get("provinceCode").toString()  );
                          cStmt.setString(7, data.get("districtCode").toString()  );
                          cStmt.setString(8, data.get("subdistrictCode").toString()  );
                          cStmt.setString(9, data.get("postal").toString()  );
                          cStmt.setString(10, data.get("updateMobile").toString()  );
                          cStmt.setString(11, data.get("memberType").toString()  );
                          cStmt.setString(12, data.get("updatedBy").toString()  );
                          cStmt.registerOutParameter(13, java.sql.Types.NUMERIC );
                          cStmt.registerOutParameter(14, java.sql.Types.VARCHAR );
                          cStmt.executeUpdate();
                          Integer size = null;
                          String errorMsg = "";
                          size = cStmt.getInt(13);
                          errorMsg = cStmt.getString(14);
                          if(size == 0 ){
                            returnResult.put("responseCode","02");
                          }else{
                            returnResult.put("responseCode","01");
                          }
                          returnResult.put("size",size);
                          returnResult.put("errorMsg",errorMsg);

                    }else if("pos_member_transaction".equalsIgnoreCase(data.get("function").toString()) ){
                          cStmt = connection.prepareCall("{call WS_JMB_API.pos_member_transaction(?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
                          cStmt.setString(1, data.get("comCode").toString()  );
                          cStmt.setString(2, data.get("memberClub").toString()  );
                          cStmt.setString(3, data.get("memberCardId").toString()  );
                          cStmt.setString(4, data.get("transactionType").toString()  );
                          cStmt.setString(5, data.get("promoCode").toString()  );
                          cStmt.setString(6, data.get("referenceDoc").toString()  );

                          Date tmpDate = data.get("referenceDate") == null?null: (Date)data.get("referenceDate");
                          cStmt.setDate(7,tmpDate ==null?null: new java.sql.Date(tmpDate.getTime()) );
                          BigDecimal tmpNetPurchase = "".equalsIgnoreCase(data.get("netPurchaseAmount").toString())  ?null:  new BigDecimal(data.get("netPurchaseAmount").toString()) ;
                          LOGGER.info("tmp Date : {}  tmpNetPurchase : {} ",tmpDate , tmpNetPurchase);
                          cStmt.setBigDecimal(8, tmpNetPurchase);
                          cStmt.setInt(9,data.get("point")==null?null: Integer.valueOf(data.get("point").toString())  );
                          cStmt.setString(10, data.get("checkBalance").toString()  );
                          cStmt.setString(11, data.get("updatedBy").toString()  );
                         // new java.sql.Date(data.get("referenceDate").toString())
                          cStmt.registerOutParameter(12, java.sql.Types.NUMERIC );
                          cStmt.registerOutParameter(13, java.sql.Types.VARCHAR );
                          cStmt.registerOutParameter(14, java.sql.Types.NUMERIC );
                          // 
                          cStmt.executeUpdate();
                          Integer size = null;
                          String errorMsg = "";
                          Integer pointBalance = null;
                          size = cStmt.getInt(12);
                          errorMsg = cStmt.getString(13);
                          pointBalance = cStmt.getInt(14);
                          LOGGER.debug("szie : {}",size);
                          if(size == 0 ){
                            returnResult.put("responseCode","02");
                          }else{
                            returnResult.put("responseCode","01");
                          }

                          returnResult.put("errorMsg",errorMsg);
                          returnResult.put("pointBalance",pointBalance);
                          
                    }else if ("get_member_history".equalsIgnoreCase(data.get("function").toString() )){
                          cStmt = connection.prepareCall("{call WS_JMB_API.GET_MEMBER_HISTORY(?,?,?,?,?,?) }");
                          cStmt.setString(1, data.get("comCode").toString()  );
                          cStmt.setString(2, data.get("memberClub").toString()  );
                          cStmt.setString(3, data.get("memberCardId").toString()  );
                          cStmt.setString(4, data.get("memberType").toString()  );
                          cStmt.registerOutParameter(5, java.sql.Types.NUMERIC );
                          cStmt.registerOutParameter(6, java.sql.Types.VARCHAR );
                          cStmt.executeUpdate();
                          Integer size = null;
                          String errorMsg = "";
                          size = cStmt.getInt(5);
                          errorMsg = cStmt.getString(6);
                          returnResult.put("size",size);
                          returnResult.put("errorMsg",errorMsg);


                    }


                      LOGGER.debug("result pl :{}",returnResult);


                    }catch(Exception e){
                        throw new RuntimeException(e);
                    }finally {
                        if (cStmt != null) {
                            cStmt.close();
                        }
                    }
                    return returnResult;
                }
            });
           return result;
        }catch(Exception e){
            LOGGER.error("error msg :{}",e);
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public  Map<String,Object> updateMemberMobilePhone(String json){
      try{
         Map<String,Object> result = null;
         LOGGER.info("[Start] updateMemberMobilePhone ");
         JSONObject jsonObject = new JSONObject(json);
         Map<String,Object> updateMap = new JSONDeserializer<Map<String, Object>>().deserialize(jsonObject.toString());
         String comCode = updateMap.get("comCode") == null?"":updateMap.get("comCode").toString();
         String memberClub = updateMap.get("memberClub") == null?"":updateMap.get("memberClub").toString();
         String memberCardId = updateMap.get("memberCardId") == null?"":updateMap.get("memberCardId").toString();
         String updateMobileNumber = updateMap.get("updateMobileNumber") == null?"":updateMap.get("updateMobileNumber").toString();
         String memberType = updateMap.get("memberType") == null?"":updateMap.get("memberType").toString();
         String updatedBy = updateMap.get("updatedBy") == null?"":updateMap.get("updatedBy").toString();
        
         Map mapCallFunction = new HashMap();
         mapCallFunction.put("comCode",comCode);
         mapCallFunction.put("memberClub",memberClub);
         mapCallFunction.put("memberCardId",memberCardId);
         mapCallFunction.put("updateMobileNumber",updateMobileNumber);
         mapCallFunction.put("memberType",memberType);
         mapCallFunction.put("updatedBy",updatedBy);

         mapCallFunction.put("function","update_member_mobile");
         result = callFunction(mapCallFunction);


         LOGGER.debug("[End] updateMemberMobilePhone ");
         return result;
      }catch(Exception e){
         LOGGER.error("error msg :{}",e);
         throw new RuntimeException(e.getMessage());
      }
    }


    @Override
    public  Map<String,Object> updateMemberProfile(String json){
      try{
         Map<String,Object> result = null;
         LOGGER.info("[Start] updateMemberProfile ");
         JSONObject jsonObject = new JSONObject(json);
         Map<String,Object> updateMap = new JSONDeserializer<Map<String, Object>>().deserialize(jsonObject.toString());
         String comCode = updateMap.get("comCode") == null?"":updateMap.get("comCode").toString();
         String memberClub = updateMap.get("memberClub") == null?"":updateMap.get("memberClub").toString();
         String memberCardId = updateMap.get("memberCardId") == null?"":updateMap.get("memberCardId").toString();
         String emailAddress = updateMap.get("emailAddress") == null?"":updateMap.get("emailAddress").toString();
         String detailAddress = updateMap.get("detailAddress") == null?"":updateMap.get("detailAddress").toString();
         String provinceCode = updateMap.get("provinceCode") == null?"":updateMap.get("provinceCode").toString();
         String districtCode = updateMap.get("districtCode") == null?"":updateMap.get("districtCode").toString();
         
         String subdistrictCode = updateMap.get("subdistrictCode") == null?"":updateMap.get("subdistrictCode").toString();
         String postal = updateMap.get("postal") == null?"":updateMap.get("postal").toString();
         String updateMobile = updateMap.get("updateMobile") == null?"":updateMap.get("updateMobile").toString();
         String memberType = updateMap.get("memberType") == null?"":updateMap.get("memberType").toString();
         String updatedBy = updateMap.get("updatedBy") == null?"":updateMap.get("updatedBy").toString();
        
         Map mapCallFunction = new HashMap();
         mapCallFunction.put("comCode",comCode);
         mapCallFunction.put("memberClub",memberClub);
         mapCallFunction.put("memberCardId",memberCardId);
         mapCallFunction.put("emailAddress",emailAddress);
         mapCallFunction.put("detailAddress",detailAddress);
         mapCallFunction.put("provinceCode",provinceCode);
         mapCallFunction.put("districtCode",districtCode);
         mapCallFunction.put("subdistrictCode",subdistrictCode);
         mapCallFunction.put("postal",postal);
         mapCallFunction.put("updateMobile",updateMobile);
         mapCallFunction.put("memberType",memberType);
         mapCallFunction.put("updatedBy",updatedBy);
         
         mapCallFunction.put("function","update_member_profile");
         result = callFunction(mapCallFunction);


         LOGGER.debug("[End] updateMemberProfile ");
         return result;
      }catch(Exception e){
         LOGGER.error("error msg :{}",e);
         throw new RuntimeException(e.getMessage());
      }
    }


    @Override
    public Map<String,Object> updatePosMemberTransaction(String json){
      try{
        LOGGER.info("updatePosMemberTransaction:{}",json);
         Map<String,Object> result = null;
         JSONObject jsonObject = new JSONObject(json);
         Map<String,Object> updateMap = new JSONDeserializer<Map<String, Object>>().deserialize(jsonObject.toString());
         String comCode = updateMap.get("comCode") == null?"":updateMap.get("comCode").toString();
         String memberClub = updateMap.get("memberClub") == null?"":updateMap.get("memberClub").toString();
         String memberCardId = updateMap.get("memberCardId") == null?"":updateMap.get("memberCardId").toString();
         String transactionType = updateMap.get("transactionType") == null?"":updateMap.get("transactionType").toString();
         String promoCode = updateMap.get("promoCode") == null?"":updateMap.get("promoCode").toString();
         
         String referenceDoc = updateMap.get("referenceDoc") == null?"":updateMap.get("referenceDoc").toString();
         String referenceDate = updateMap.get("referenceDate") == null?"":updateMap.get("referenceDate").toString();
         
         String netPurchaseAmount = updateMap.get("netPurchaseAmount") == null?"":updateMap.get("netPurchaseAmount").toString();
         String point = updateMap.get("point") == null?"":updateMap.get("point").toString();
         String checkBalance = updateMap.get("checkBalance") == null?"":updateMap.get("checkBalance").toString();
         String updatedBy = updateMap.get("updatedBy") == null?"":updateMap.get("updatedBy").toString();
         Map mapCallFunction = new HashMap();
         
        // referenceDate  yyyy-mm-dd
         Date refDate = null;
         if(!referenceDate.equalsIgnoreCase("") ){
          refDate = DATE_FORMAT_PARAM.parse(referenceDate);
         }

         mapCallFunction.put("comCode",comCode); 
         mapCallFunction.put("memberClub",memberClub); 
         mapCallFunction.put("memberCardId",memberCardId); 
         mapCallFunction.put("transactionType",transactionType); 
         mapCallFunction.put("promoCode",promoCode); 
         mapCallFunction.put("referenceDoc",referenceDoc); 
         mapCallFunction.put("referenceDate",refDate); 
         mapCallFunction.put("netPurchaseAmount",netPurchaseAmount); 
         mapCallFunction.put("point",point); 
         mapCallFunction.put("checkBalance",checkBalance); 
         mapCallFunction.put("updatedBy",updatedBy); 
        
         mapCallFunction.put("function","pos_member_transaction");
         result = callFunction(mapCallFunction);

        LOGGER.debug("[End] updatePosMemberTransaction ");
         return result;
      }catch(Exception e){
         LOGGER.error("error msg :{}",e);
         throw new RuntimeException(e.getMessage());
      }
    }



    @Override
    public Map<String,Object> getMemberHistory(String json){
      try{
        LOGGER.info("getMemberHistory  : {} ",json);
         Map<String,Object> returnResult = new HashMap<>();
         JSONObject jsonObject = new JSONObject(json);
         Map<String,Object> updateMap = new JSONDeserializer<Map<String, Object>>().deserialize(jsonObject.toString());
         String comCode = updateMap.get("comCode") == null?"":updateMap.get("comCode").toString();
         String memberClub = updateMap.get("memberClub") == null?"":updateMap.get("memberClub").toString();
         String memberCardId = updateMap.get("memberCardId") == null?"":updateMap.get("memberCardId").toString();
         String memberType = updateMap.get("memberType") == null?"":updateMap.get("memberType").toString();
         Map result = null;
         Map mapCallFunction = new HashMap();
         
          mapCallFunction.put("comCode",comCode);
          mapCallFunction.put("memberClub",memberClub);
          mapCallFunction.put("memberCardId",memberCardId);
          mapCallFunction.put("memberType",memberType);
          mapCallFunction.put("function","get_member_history");
          result = callFunction(mapCallFunction);
          List<Map<String,Object>> data = new ArrayList<>();
          if(  Integer.valueOf( result.get("size").toString() ) > 0  ){
            data = findMemberHistoryData();
            if(data.size()>0){
              returnResult.put("responseCode","01");
              returnResult.put("responseMsg","");
            }else{
              returnResult.put("responseCode","02");
              returnResult.put("responseMsg","Not found member histories data");
            }
           
            returnResult.put("histories",data);

          }else{
            returnResult.put("responseCode","02");
            returnResult.put("responseMsg",result.get("errorMsg"));
            returnResult.put("histories",data);

          }          
          return returnResult;
      }catch(Exception e){
         LOGGER.error("error msg :{}",e);
         throw new RuntimeException(e.getMessage());
      }
    }

    public List<Map<String,Object>> findMemberHistoryData(){
      try{
        LOGGER.info("findMemberHistoryData");
        StringBuilder sqlStatement  = new StringBuilder();
        sqlStatement.append("SELECT COM_CODE , MEMBERCLUB , MEMBER_CARDID , TYPE , TYPE_DESC \n");
        sqlStatement.append(" ,DOC_DATE , SEQ , POINT_IN , POINT_OUT , REDEEM , BRANCH , DOC_NO  , REASON \n");
        sqlStatement.append(" ,OPTUPD_DATE , REF_PROG , OPTUPD_BY , ADJ_REMARK \n");
        sqlStatement.append(" FROM  v$ws_member_history \n");

        Query query = entityManager.createNativeQuery(sqlStatement.toString());
        List<Object[]> rows = null;
        List<Map<String,Object>> result = new ArrayList<>();
        rows = query.getResultList();
            LOGGER.info("findMemberHistoryData size :{} ",rows.size());

        for(Object[] col :rows){
              Map<String,Object> mapResult = new HashMap();
              mapResult.put("comCode",col[0]);
              mapResult.put("memberClub",col[1]);
              mapResult.put("memberCardId",col[2]);
              mapResult.put("type",col[3]);
              mapResult.put("typeDesc",col[4]);
              mapResult.put("docDate",col[5]);
              mapResult.put("seq",col[6]);
              mapResult.put("pointIn",col[7]);
              mapResult.put("pointOut",col[8]);
              mapResult.put("redeem",col[9]);
              mapResult.put("branch",col[10]);
              mapResult.put("docNo",col[11]);
              mapResult.put("reason",col[12]);
              mapResult.put("optUpdDate",col[13]);
              mapResult.put("refProg",col[14]);
              mapResult.put("optupdBy",col[15]);
              mapResult.put("adjRemark",col[16]);
              
              result.add(mapResult);
            }
            LOGGER.info("[02]findMemberHistoryData return :{}",result.size());
            return result;
      }catch(Exception e){
         LOGGER.error("error msg :{}",e);
         throw new RuntimeException(e.getMessage());
      } 
    }





}

 
