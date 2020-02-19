package com.ss.app.webservice.service.impl;


import com.ss.app.webservice.domain.application.OrderHeader;
import com.ss.app.webservice.domain.application.OrderDetail;
import com.ss.app.webservice.domain.application.PaymentHeader;
import com.ss.app.webservice.domain.application.PaymentDetail;
import com.ss.app.webservice.domain.application.VoidPayment;

import com.ss.app.webservice.domain.application.VoidOrder;

import com.ss.app.webservice.repository.OrderDetailRepository;
import com.ss.app.webservice.repository.OrderHeaderRepository;
import com.ss.app.webservice.repository.PaymentDetailRepository;
import com.ss.app.webservice.repository.PaymentHeaderRepository;
import com.ss.app.webservice.repository.VoidOrderRepository;
import com.ss.app.webservice.repository.VoidPaymentRepository;


import com.ss.app.webservice.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Set;

import java.util.LinkedHashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Connection;




import org.hibernate.Session;

import org.hibernate.jdbc.ReturningWork; 
import java.math.BigDecimal;

import org.json.JSONObject;
import flexjson.JSONDeserializer;


import org.json.*;

import com.ss.app.webservice.repository.custom.ParameterRepositoryCustom;

import java.text.SimpleDateFormat;

import com.google.gson.*;
import org.springframework.http.*;






import java.util.*;





//






import org.springframework.security.core.GrantedAuthority;



import org.springframework.web.client.RestTemplate;





import java.lang.reflect.Type;


import java.util.*;
import com.ss.app.webservice.service.GrantedAuthorityTypeAdaptor;

@Service
public class TransactionServiceImpl implements TransactionService {

  protected JsonParser parser = new JsonParser();

    JsonSerializer<Date> ser = new JsonSerializer<Date>() {
        public JsonElement serialize(Date src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            return src == null ? null : new JsonPrimitive(src.getTime());
        }
    };
    
    JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) {
            return json == null ? null : new Date(json.getAsLong());
        }
    };


    protected Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm").registerTypeAdapter(Date.class, deser).registerTypeAdapter(GrantedAuthority.class, new GrantedAuthorityTypeAdaptor()).create();


    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private static final String ZERO_NUMBER = "0";
    private static final String FN_VALIDATE_ORDER = "{? = call WS_JMB_API.Validate_Order(?,?,?,?) }";
    private static final String FN_GET_POST_TRACKNO = "{? = call WS_JMB_API.Validate_Order(?,?,?) }";
    private static final String FN_GET_WSTOKENAUTH = "{? = call WS_JMB_API.get_WSTOKENAUTH(?,?,?) }";
    private static final String FN_GET_WSFIXEDCODE = "{? = call WS_JMB_API.get_WSFIXEDCODE(?,?,?) }";
    private static final String FN_VOID_ORDER = "{? = call WS_JMB_API.void_Order(?,?,?) }";
    private static final String FN_VALIDATE_PAYMENT = "{? = call WS_JMB_API.Validate_Payment(?,?,?) }";
    private static final String FN_VOID_PAYMENT = "{? = call WS_JMB_API.void_Payment(?,?,?) }";
    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");


    @PersistenceContext
    private EntityManager em;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderHeaderRepository orderHeaderRepository;
    
    @Autowired
    PaymentHeaderRepository paymentHeaderRepository;

    @Autowired
    PaymentDetailRepository paymentDetailRepository;

    @Autowired
    VoidOrderRepository voidOrderRepository;

    @Autowired
    VoidPaymentRepository voidPaymentRepository;

    @Autowired
    ParameterRepositoryCustom parameterRepositoryCustom;

    @Override
    public Map callFunction(Map<String,Object> data){
        try{
             
             Session session = em.unwrap(Session.class);
             Map result = null;
              result = session.doReturningWork(new ReturningWork<Map>() {
                @Override
                public Map execute(Connection connection) throws SQLException {
                    CallableStatement cStmt  = null;
                    Map returnResult = new HashMap<>();
                    try {
                    LOGGER.debug("data : {}",data);
                        if(  "WS_JMB_API.Validate_Order".equalsIgnoreCase(data.get("functionName").toString()) ){
                          cStmt = connection.prepareCall(FN_VALIDATE_ORDER);
                          cStmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                          cStmt.setLong(2, Long.valueOf( data.get("id").toString())  );

                          cStmt.registerOutParameter(3, java.sql.Types.VARCHAR );
                          cStmt.registerOutParameter(4, java.sql.Types.NUMERIC );
                          cStmt.registerOutParameter(5, java.sql.Types.VARCHAR );
                          String v_message = "";
                          Integer po_status = null;
                          String po_error = "";
                          cStmt.executeUpdate();
                          v_message = cStmt.getString(1);
                          String runningNummber = cStmt.getString(3);
                          po_status = cStmt.getInt(4);
                          po_error = cStmt.getString(5);

                          returnResult.put("message",v_message);
                          returnResult.put("status",po_status);   //0: Complete, 1 : Error JMB Validate, 2 : Error on Customer Validate, 3 : Error on Order Validate
                          returnResult.put("errorMsg",po_error);
                          returnResult.put("posTrackNumber",runningNummber);

                        }else if ("WS_JMB_API.get_POSTrackNo".equalsIgnoreCase(data.get("functionName").toString())){
                          cStmt = connection.prepareCall(FN_GET_POST_TRACKNO);
                          cStmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                          cStmt.setString(2, data.get("comCode").toString()  );
                          cStmt.setString(3, data.get("branch").toString()  );

                          cStmt.executeUpdate();
                          String posTrackNo = cStmt.getString(1);
                          returnResult.put("posTrackNo",posTrackNo);


                        }else if ("WS_JMB_API.get_WSTOKENAUTH".equalsIgnoreCase(data.get("functionName").toString())  ){
                          cStmt = connection.prepareCall(FN_GET_WSTOKENAUTH);
                          cStmt.registerOutParameter(1, java.sql.Types.NUMERIC);
                          cStmt.registerOutParameter(2, java.sql.Types.VARCHAR);
                          cStmt.registerOutParameter(3, java.sql.Types.VARCHAR);
                          cStmt.registerOutParameter(4, java.sql.Types.VARCHAR);
                          cStmt.executeUpdate();
                          String user = cStmt.getString(2)  ;
                          String password = cStmt.getString(3);
                          String role = cStmt.getString(4);
                          returnResult.put("user",user);
                          returnResult.put("password",password);
                          returnResult.put("role",role);
                        }else if("WS_JMB_API.get_WSFIXEDCODE".equalsIgnoreCase(data.get("functionName").toString()) ){
                          cStmt = connection.prepareCall(FN_GET_WSFIXEDCODE);
                          cStmt.registerOutParameter(1, java.sql.Types.NUMERIC);
                          cStmt.registerOutParameter(2, java.sql.Types.VARCHAR);
                          cStmt.registerOutParameter(3, java.sql.Types.VARCHAR);
                          cStmt.registerOutParameter(4, java.sql.Types.VARCHAR);
                          cStmt.executeUpdate();
                          String comCode =   cStmt.getString(2) == null ? "JAY" :cStmt.getString(2);
                          String vatCode =   cStmt.getString(3) == null ? "07" :cStmt.getString(3);
                          String voidService =   cStmt.getString(4) == null ? "VOID" :cStmt.getString(4);
                          returnResult.put("comCode",comCode);
                          returnResult.put("vatCode",vatCode);
                          returnResult.put("voidService",voidService);
                        }else if("WS_JMB_API.void_Order".equalsIgnoreCase(data.get("functionName").toString()) ){
                          cStmt = connection.prepareCall(FN_VOID_ORDER);
                          cStmt.registerOutParameter(1, java.sql.Types.NUMERIC);
                          cStmt.setString(2, data.get("comCode").toString()  );
                          cStmt.setString(3, data.get("posTrackNumber").toString()  );
                          cStmt.registerOutParameter(4, java.sql.Types.VARCHAR);
                          cStmt.executeUpdate();
                          Integer po_status = cStmt.getInt(1);
                          String po_error = cStmt.getString(4);
                          returnResult.put("status",po_status);   //0: Complete, 1 : Error JMB Validate, 2 : Error on Customer Validate, 3 : Error on Order Validate
                          returnResult.put("errorMsg",po_error);
                        }else if("WS_JMB_API.Validate_Payment".equalsIgnoreCase(data.get("functionName").toString()) ){
                          cStmt = connection.prepareCall(FN_VALIDATE_PAYMENT);
                          cStmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                          cStmt.setLong(2, Long.valueOf( data.get("id").toString())  );
                          cStmt.registerOutParameter(3, java.sql.Types.NUMERIC );
                          cStmt.registerOutParameter(4, java.sql.Types.VARCHAR );
                          cStmt.executeUpdate();
                          String v_message = "";
                          Integer po_status = null;
                          String po_error = "";
                          v_message = cStmt.getString(1);
                          po_status = cStmt.getInt(3);
                          po_error = cStmt.getString(4);
                          returnResult.put("message",v_message);
                          returnResult.put("status",po_status);   //0: Complete, 1 : Error JMB Validate, 2 : Error on Customer Validate, 3 : Error on Order Validate
                          returnResult.put("errorMsg",po_error);
                      
                        }else if("WS_JMB_API.void_Payment".equalsIgnoreCase(data.get("functionName").toString())){
                           cStmt = connection.prepareCall(FN_VOID_PAYMENT);
                          cStmt.registerOutParameter(1, java.sql.Types.NUMERIC);
                          cStmt.setString(2, data.get("comCode").toString()  );
                          cStmt.setString(3, data.get("posTrackNumber").toString()  );
                          cStmt.registerOutParameter(4, java.sql.Types.VARCHAR);
                          cStmt.executeUpdate();
                          Integer po_status = cStmt.getInt(1);
                          String po_error = cStmt.getString(4);
                          returnResult.put("status",po_status);   //0: Complete, 1 : Error JMB Validate, 2 : Error on Customer Validate, 3 : Error on Order Validate
                          returnResult.put("errorMsg",po_error);
                        }


                        LOGGER.debug(" get function Result :  {}",returnResult);
                     
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
            LOGGER.error("error msg:{}",e);

            throw new RuntimeException(e);
        }
    }

    @Override
    public Map orderItem(String json){
        LOGGER.info("[01] Order Item ");
        try{
           JSONObject jsonObject = new JSONObject(json);
           Map<String,Object> orderDetails = new JSONDeserializer<Map<String,Object>>().deserialize(jsonObject.toString());
           List<Map<String,Object>> dtlData = new JSONDeserializer<List<Map<String,Object>>>().deserialize(jsonObject.get("item").toString());




           
          

           OrderHeader orderHeader = new OrderHeader(); 
           orderHeader.setCreatedDate(new Date());
           orderHeader.setInfStatus( Integer.valueOf( ZERO_NUMBER) );

           orderHeader.setComCode(  orderDetails.get("comCode")==null?null:orderDetails.get("comCode").toString() );
           orderHeader.setBranchCode(  orderDetails.get("branchCode")==null?null:orderDetails.get("branchCode").toString() );
           orderHeader.setSalePerson(  orderDetails.get("salePerson")==null?null:orderDetails.get("salePerson").toString() );
           orderHeader.setOrderType(  orderDetails.get("orderType")==null?null:orderDetails.get("orderType").toString() );
           orderHeader.setCustomerName(  orderDetails.get("customerName")==null?null:orderDetails.get("customerName").toString() );
           orderHeader.setCustomerGroup(  orderDetails.get("customerGroup")==null?null:orderDetails.get("customerGroup").toString() );
           orderHeader.setAddressLine1(  orderDetails.get("addressLine1")==null?null:orderDetails.get("addressLine1").toString() );
           orderHeader.setAddressLine2(  orderDetails.get("addressLine2")==null?null:orderDetails.get("addressLine2").toString() );
           orderHeader.setSubDistrict(  orderDetails.get("subDistrict")==null?null:orderDetails.get("subDistrict").toString() );
           orderHeader.setDistrict(  orderDetails.get("district")==null?null:orderDetails.get("district").toString() );
           orderHeader.setProvince(  orderDetails.get("province")==null?null:orderDetails.get("province").toString() );
           orderHeader.setZipCode(  orderDetails.get("zipCode")==null?null:orderDetails.get("zipCode").toString() );
           orderHeader.setTelephoneNumber(  orderDetails.get("telephoneNumber")==null?null:orderDetails.get("telephoneNumber").toString() );
           orderHeader.setTaxId(  orderDetails.get("taxId")==null?null:orderDetails.get("taxId").toString() );
           orderHeader.setMemberCode(  orderDetails.get("memberCode")==null?null:orderDetails.get("memberCode").toString() );
           orderHeader.setMemberId(  orderDetails.get("memberId")==null?null:orderDetails.get("memberId").toString() );
           orderHeader.setDeliveryAddress(  orderDetails.get("deliveryAddress")==null?null:orderDetails.get("deliveryAddress").toString() );

           orderHeader.setVatCode(  orderDetails.get("vatCode")==null?null:orderDetails.get("vatCode").toString() );
           orderHeader.setPreOrderNumber(  orderDetails.get("preOrderNumber")==null?null:orderDetails.get("preOrderNumber").toString() );
           orderHeader.setSource(  orderDetails.get("source")==null?null:orderDetails.get("source").toString() );
         
           if( orderDetails.get("grandTotal")  == null || !"".equalsIgnoreCase( orderDetails.get("grandTotal").toString())   ){
               orderHeader.setGrandTotal(  orderDetails.get("grandTotal")==null? new BigDecimal(ZERO_NUMBER) :  new BigDecimal( orderDetails.get("grandTotal").toString()) );
           }
          

           Set<OrderDetail> orderDetailDatas = new  LinkedHashSet<>();
           for(Map<String,Object> data:dtlData){
              OrderDetail orderDtl = new OrderDetail();
              orderDtl.setItemCode(data.get("itemCode")==null?null:data.get("itemCode").toString()  );
              orderDtl.setItemName(data.get("itemName")==null?null:data.get("itemName").toString()  );
              orderDtl.setUom(data.get("UOM")==null?null:data.get("UOM").toString()  );
              orderDtl.setPriceList(data.get("priceList")==null?null:data.get("priceList").toString()  );
             
               if( data.get("orderQuantities") == null ||  !"".equalsIgnoreCase( data.get("orderQuantities").toString()) ){
                  orderDtl.setOrderQuantities(data.get("orderQuantities")==null?null: Integer.valueOf( data.get("orderQuantities").toString())  );
                }  
                if( data.get("shipQuantities") == null ||  !"".equalsIgnoreCase( data.get("shipQuantities").toString() )){
                   orderDtl.setShipQuantities(data.get("shipQuantities")==null?null: Integer.valueOf( data.get("shipQuantities").toString())  );
                }
                if( data.get("pricePerUnit") == null ||  !"".equalsIgnoreCase( data.get("pricePerUnit").toString())){
                   orderDtl.setPricePerUnit(data.get("pricePerUnit")==null?null: new BigDecimal ( data.get("pricePerUnit").toString())  );
                }

              orderDtl.setPromotionLv1(  data.get("promotionLv1")==null?null:data.get("promotionLv1").toString());
              orderDtl.setPromotionLv2(  data.get("promotionLv2")==null?null:data.get("promotionLv2").toString());
              orderDtl.setPromotionLv3(  data.get("promotionLv3")==null?null:data.get("promotionLv3").toString());
              orderDtl.setDiscountLv1(  data.get("discountLv1")==null?null:data.get("discountLv1").toString());
              orderDtl.setDiscountLv2(  data.get("discountLv2")==null?null:data.get("discountLv2").toString());
              orderDtl.setDiscountLv3(  data.get("discountLv3")==null?null:data.get("discountLv3").toString());
              orderDtl.setOrderHeader(orderHeader);

              orderDetailDatas.add(orderDtl);

           }
           orderHeader.setOrderDetails(orderDetailDatas);
           orderHeaderRepository.saveAndFlush(orderHeader);
           Map<String,Object> callValidateData = new HashMap<>();
           callValidateData.put("functionName","WS_JMB_API.Validate_Order");
           callValidateData.put("id",orderHeader.getId());
           Map<String,Object> result = callFunction(callValidateData);
           orderHeaderRepository.flush();
           OrderHeader getOrderCreated = orderHeaderRepository.findOne(orderHeader.getId()); 
           LOGGER.debug("POS TRACK NUMBER : {}",getOrderCreated.getPosTrackNumber());


          String posTrackNumber ="";
           if( Integer.valueOf(result.get("status").toString())  == 0 ){

               result.put("errorCode","01");
                posTrackNumber = result.get("posTrackNumber") == null?"":result.get("posTrackNumber").toString();
                           
           }else{


               result.put("errorCode","02");
           
           }


          
           result.put("posTrackNumber",  posTrackNumber );
           return result;

        }catch(Exception e){
            LOGGER.error("error msg:{}",e);

            throw new RuntimeException(e);
        }
    }

    @Override
    public Map voidOrder(String json){
      try{
         JSONObject jsonObject = new JSONObject(json);
         
         Map<String,Object> voidJson = new JSONDeserializer<Map<String,Object>>().deserialize(jsonObject.toString());
         LOGGER.debug("[01] voidOrder  :{}  ",voidJson.get("posTrackNumber")); 
         VoidOrder voidOrder = new VoidOrder();
         Date today = new Date();
         voidOrder.setCreatedDate(today);
         voidOrder.setInfStatus(Integer.valueOf(ZERO_NUMBER));
         voidOrder.setSource( voidJson.get("source")== null? "": voidJson.get("source").toString() );
         voidOrder.setPosTrackNumber( voidJson.get("posTrackNumber")== null? "": voidJson.get("posTrackNumber").toString() );
         voidOrder.setReason( voidJson.get("reason")== null? "": voidJson.get("reason").toString()  );
         voidOrder.setComCode( voidJson.get("comCode")== null? "": voidJson.get("comCode").toString()  );
         voidOrder.setBranchCode( voidJson.get("branchCode")== null? "": voidJson.get("branchCode").toString()  );
         voidOrder.setCustomerName( voidJson.get("customerName")== null? "": voidJson.get("customerName").toString()  );
         voidOrder.setCustomerGroup( voidJson.get("customerGroup")== null? "": voidJson.get("customerGroup").toString()  );

         voidOrderRepository.saveAndFlush(voidOrder);
         Map<String,Object> result = null;


           Map<String,Object> callFunctionMap = new HashMap<>();
           callFunctionMap.put("functionName","WS_JMB_API.void_Order");
           callFunctionMap.put("comCode", voidOrder.getComCode()  );
           callFunctionMap.put("posTrackNumber", voidOrder.getPosTrackNumber()  );
           result = callFunction(callFunctionMap);
           if( Integer.valueOf(result.get("status").toString())  == 0 ){
              voidOrder.setInfStatus( Integer.valueOf("1")  );
              result.put("status","01");
           }else{
              voidOrder.setInfStatus( Integer.valueOf("2")  );
              result.put("status","02");
           }
          



         LOGGER.debug("[02] save Void Order :{} ",voidOrder.getId());
         LOGGER.debug("get :{}",result);
       return result;
      }catch(Exception e){
        LOGGER.error("error msg:{}",e);

        throw new RuntimeException(e);
      }
    }


    @Override
    public Map paymentInvoice(String json){
        LOGGER.info("payment invoice");
        try{
           JSONObject jsonObject = new JSONObject(json);
           Map<String,Object> paymentJson = new JSONDeserializer<Map<String,Object>>().deserialize(jsonObject.toString());
           List<Map<String,Object>> paymentDtl = new JSONDeserializer<List<Map<String,Object>>>().deserialize(jsonObject.get("payment").toString());
           LOGGER.debug("[01] payment :{}",paymentJson);
           PaymentHeader paymentHeader = new PaymentHeader();
           Date today = new Date();




           
           paymentHeader.setCreatedDate(today);
           paymentHeader.setInfStatus( Integer.valueOf( ZERO_NUMBER) );
           paymentHeader.setComCode( paymentJson.get("comCode")==null?null:paymentJson.get("comCode").toString() );
           paymentHeader.setBranchCode(  paymentJson.get("branchCode")==null?null:paymentJson.get("branchCode").toString() );
           paymentHeader.setMerchantId(  paymentJson.get("merchantId")==null?null:paymentJson.get("merchantId").toString() );
           paymentHeader.setSource(  paymentJson.get("source")==null?null:paymentJson.get("source").toString() );
           paymentHeader.setPayType(  paymentJson.get("payType")==null?null:paymentJson.get("payType").toString() );
           paymentHeader.setPreOrderNumber(  paymentJson.get("preOrderNumber")==null?null:paymentJson.get("preOrderNumber").toString() );
           paymentHeader.setInvoiceNo(  paymentJson.get("invoiceNo")==null?null:paymentJson.get("invoiceNo").toString() );
           paymentHeader.setProductCode(  paymentJson.get("productCode")==null?null:paymentJson.get("productCode").toString() );

           if( paymentJson.get("redemption")==null || !"".equalsIgnoreCase(paymentJson.get("redemption").toString()) ){
              paymentHeader.setRedemption(  paymentJson.get("redemption")==null?new BigDecimal(ZERO_NUMBER):new BigDecimal(paymentJson.get("redemption").toString()) );
           }
          
           paymentHeader.setMemberGroup(  paymentJson.get("memberGroup")==null?null:paymentJson.get("memberGroup").toString() );
           paymentHeader.setMemberId(  paymentJson.get("memberId")==null?null:paymentJson.get("memberId").toString() );
           Set<PaymentDetail> paymentDetails = new  LinkedHashSet<>();
           for(Map<String,Object>  payDtl : paymentDtl){
              PaymentDetail dtl = new PaymentDetail();
              if( payDtl.get("paidAmount") == null || !"".equalsIgnoreCase( payDtl.get("paidAmount").toString() )  ){
                dtl.setPaidAmount(  payDtl.get("paidAmount")==null? new BigDecimal(ZERO_NUMBER) :new BigDecimal(payDtl.get("paidAmount").toString()));
              }
              dtl.setPaymentType(  payDtl.get("paymentType")==null?null:payDtl.get("paymentType").toString());
              dtl.setPaymentCode(  payDtl.get("paymentCode")==null?null:payDtl.get("paymentCode").toString());
              
              dtl.setCardNo(  payDtl.get("cardNo")==null?null:payDtl.get("cardNo").toString());
              dtl.setCurrency(  payDtl.get("currency")==null?null:payDtl.get("currency").toString());
              dtl.setExchangeRate(  payDtl.get("exchangeRate")==null?null:new BigDecimal (payDtl.get("exchangeRate").toString() ));
              dtl.setPaidCoin(  payDtl.get("paidCoin")==null?null:new BigDecimal (payDtl.get("paidCoin").toString() ));
              Date exchaneDate = payDtl.get("exchangeDate") == null ? null : DATE_FORMAT.parse(  payDtl.get("exchangeDate").toString()   );
              dtl.setExchangeDate( exchaneDate  );
              


              dtl.setPaymentHeader(paymentHeader);
              paymentDetails.add(dtl);
           }

           paymentHeader.setPaymentDtls(paymentDetails);
           LOGGER.debug("paymentDtl size : {}",paymentHeader.getPaymentDtls().size() );
           paymentHeaderRepository.saveAndFlush(paymentHeader);
           LOGGER.debug("[02] save payment :{} ",paymentHeader.getId());
           Map<String,Object> callFunctionMap = new HashMap<>();
           callFunctionMap.put("functionName","WS_JMB_API.Validate_Payment");
           callFunctionMap.put("id",paymentHeader.getId());
           Map<String,Object> result =null;
           result= callFunction(callFunctionMap);
           PaymentHeader payment = paymentHeaderRepository.findOne(paymentHeader.getId());
           LOGGER.debug("[01]=====================================");
           payment.setErrorCode(result.get("status").toString() );
           payment.setUpdateDate(new Date() );
           if( Integer.valueOf(result.get("status").toString())  == 0 ){
               payment.setInfStatus( Integer.valueOf( "1" ) );
              result.put("status","01");
           }else{
               payment.setInfStatus( Integer.valueOf( "2" ) );
               payment.setErrorMsg(result.get("errorMsg").toString() );
               result.put("status","02");
           }
           LOGGER.debug("[02]=====================================");
           paymentHeaderRepository.saveAndFlush(payment);
           LOGGER.debug("[03]=====================================");
           
           result.put("trackNumber",payment.getPreOrderNumber() );

           return result;
        }catch(Exception e){
            LOGGER.error("error msg:{}",e);

            throw new RuntimeException(e);
        }
    }
   
   @Override
   public Map voidPaymentInvoice(String json){
     LOGGER.info("void payment");
     try{

         LOGGER.debug("[01] save voidpayment : ");
        
         JSONObject jsonObject = new JSONObject(json);
         Map<String,Object> voidPaymentJson = new JSONDeserializer<Map<String,Object>>().deserialize(jsonObject.toString());
         VoidPayment voidPayment = new VoidPayment();
         Date today = new Date();
         Map<String,Object> result = null;
         Map<String,Object> constaintRequest = new HashMap<>();
         constaintRequest.put("functionName","WS_JMB_API.get_WSFIXEDCODE");
         Map<String,Object> constaintData =  callFunction(constaintRequest);
         voidPayment.setInfStatus(Integer.valueOf( ZERO_NUMBER));
         voidPayment.setCreatedDate(today);
         voidPayment.setCallService(voidPaymentJson.get("callService")==  null ? constaintData.get("voidService").toString()   :voidPaymentJson.get("callService").toString() );
         voidPayment.setTrackNumber(voidPaymentJson.get("trackNumber")==null?null:voidPaymentJson.get("trackNumber").toString());
         voidPayment.setMerchantId( voidPaymentJson.get("merchantId")==null?null:voidPaymentJson.get("merchantId").toString() );
         voidPayment.setReason(voidPaymentJson.get("reason")==null?null:voidPaymentJson.get("reason").toString());
         voidPaymentRepository.saveAndFlush(voidPayment);
         LOGGER.debug("[02] save voidPayment :{} ",voidPayment.getId());


           Map<String,Object> callFunctionMap = new HashMap<>();
           callFunctionMap.put("functionName","WS_JMB_API.void_Payment");

           callFunctionMap.put("comCode", "JAY"  );

           callFunctionMap.put("posTrackNumber", voidPayment.getTrackNumber() );

           result = callFunction(callFunctionMap);
           if( Integer.valueOf(result.get("status").toString())  == 0 ){
              voidPayment.setInfStatus( Integer.valueOf("1")  );
              result.put("status","01");
           }else{
              LOGGER.debug("ERROR Interface");
              voidPayment.setInfStatus( Integer.valueOf("2")  );
              result.put("status","02");
           }
           voidPayment.setErrorMsg( result.get("errorMsg") == null ? null : result.get("errorMsg").toString()   );
           voidPaymentRepository.saveAndFlush(voidPayment);




          result.put("remark","");

          LOGGER.debug("return result : {} ",result);

         return result;
     }catch(Exception e){
        LOGGER.error("error msg:{}",e);

        throw new RuntimeException(e);
     }
   }

  @Override
  public String testjfin(){
    try{
      LOGGER.info("testJfin");
        RestTemplate restTemplate = new RestTemplate();
        String resultString= "";
        MediaType mediaType = new MediaType("application","json", StandardCharsets.UTF_8);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
          headers.add("X-API-KEY", "l487kBwGEf09JmSF02Q5wVnuXcTHvZYz");
          Map data = new HashMap();
          data.put("merchant_id","2uXI81");
          data.put("order_id","earth001");
            String json = gson.toJson(data);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);




            ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.int.jfin.network/void_order",  HttpMethod.POST, entity, String.class);
            resultString =responseEntity.getBody() ;
            LOGGER.debug(" Result : {}",resultString);
            return resultString;
    }catch(Exception e){
        LOGGER.error("error msg:{}",e);

        throw new RuntimeException(e);
    }
       
  }

 
}