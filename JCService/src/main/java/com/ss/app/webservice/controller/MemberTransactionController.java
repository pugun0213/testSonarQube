package com.ss.app.webservice.controller;


import com.ss.app.webservice.service.MemberTransactionService;
import com.ss.app.webservice.service.TransactionService;

import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/rest")
public class MemberTransactionController {
    private Logger logger = LoggerFactory.getLogger(MemberTransactionController.class);
    private static final String headersB="application/json; charset=utf-8";
    private static final String headersA="Content-Type";
    private static final String status= "status";
    private static final String errorMsg= "errorMsg";

    @Autowired
    MemberTransactionService memberTransactionService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/members/getMemberHistory")
    public ResponseEntity<String> getMemberHistory(@RequestBody String json){
      HttpHeaders headers = new HttpHeaders();
      headers.add(headersA, headersB);
      Map result = new HashMap<>();
      try{
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(memberTransactionService.getMemberHistory(json))),headers, HttpStatus.OK);
      }catch(Exception e){
            logger.error("error msg1 : {} ",e.getMessage());
            headers.add(status,"02");
            headers.add(errorMsg,e.getMessage());
            result.put(status,"02");
            result.put(errorMsg,e.getMessage());
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.OK);
      }

    }

    @PostMapping("/members/getPosMemberLogin")
    public ResponseEntity<String> getPosMemberLogin(@RequestBody String json){
      HttpHeaders headers = new HttpHeaders();
      headers.add(headersA, headersB);
      Map result = new HashMap<>();
      try{
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(memberTransactionService.getPosMemberLogin(json))),headers, HttpStatus.OK);
      }catch(Exception e){
            logger.error("error msg2 : {} ",e.getMessage());
            headers.add(status,"02");
            headers.add(errorMsg,e.getMessage());
            result.put(status,"02");
            result.put(errorMsg,e.getMessage());
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.OK);
      }

    }

    @PostMapping("/members/getMemberProfile")
    public ResponseEntity<String> getMemberProfile(@RequestBody String json){
      HttpHeaders headers = new HttpHeaders();
      headers.add(headersA, headersB);
      Map result = new HashMap<>();
      try{
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(memberTransactionService.getMemberProfile(json))),headers, HttpStatus.OK);
      }catch(Exception e){
            logger.error("error msg3 : {} ",e.getMessage());
            headers.add(status,"02");
            headers.add(errorMsg,e.getMessage());
            result.put(status,"02");
            result.put(errorMsg,e.getMessage());
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.OK);
      }

    }

    @PostMapping("/members/updatMembereMobile")
    public ResponseEntity<String> updatMembereMobile(@RequestBody String json){
      HttpHeaders headers = new HttpHeaders();
      headers.add(headersA, headersB);
      Map result = new HashMap<>();
      try{  
        
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(memberTransactionService.updateMemberMobilePhone(json))),headers, HttpStatus.OK);
      }catch(Exception e){
            logger.error("error msg4 : {} ",e.getMessage());
            headers.add(status,"02");
            headers.add(errorMsg,e.getMessage());
            result.put(status,"02");
            result.put(errorMsg,e.getMessage());
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.OK);
      }

    }

    @PostMapping("/members/updateMemberProfile")
    public ResponseEntity<String> updateMemberProfile(@RequestBody String json){
      HttpHeaders headers = new HttpHeaders();
      headers.add(headersA, headersB);
      Map result = new HashMap<>();
      try{  
        
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(memberTransactionService.updateMemberProfile(json))),headers, HttpStatus.OK);
      }catch(Exception e){
            logger.error("error msg5 : {} ",e.getMessage());
            headers.add(status,"02");
            headers.add(errorMsg,e.getMessage());
            result.put(status,"02");
            result.put(errorMsg,e.getMessage());
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.OK);
      }

    }

    
    @PostMapping("/members/updatePosMemberTransaction")
    public ResponseEntity<String> updatePosMemberTransaction(@RequestBody String json){
      HttpHeaders headers = new HttpHeaders();
      headers.add(headersA, headersB);
      Map result = new HashMap<>();
      try{  
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(memberTransactionService.updatePosMemberTransaction(json))),headers, HttpStatus.OK);
      }catch(Exception e){
            logger.error("error msg6 : {} ",e.getMessage());
            headers.add(status,"02");
            headers.add(errorMsg,e.getMessage());
            result.put(status,"02");
            result.put(errorMsg,e.getMessage());
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.OK);
      }

    }
    
    @PostMapping("/orders/items")
    public ResponseEntity<String> orderItems(HttpServletRequest request,@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(headersA, headersB);
        logger.info("[orderItems][Controller] ");
        try{    
            logger.debug("json1 :{}",json);



            Map result =  transactionService.orderItem(json);
            headers.add(status,"01");

            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.OK);
       
        }catch(Exception e){
            logger.error("error msg7 : {}",e.getMessage());
            Map result =  new HashMap<>();
            headers.add(status,"02");
            headers.add(errorMsg,e.getMessage());
            result.put(status,"02");
            result.put(errorMsg,e.getMessage());
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }  

    @PostMapping("/orders/void")
    public ResponseEntity<String> voidOrders(HttpServletRequest request,@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(headersA, headersB);
        logger.info("[voidOrders][Controller] ");
        try{    
            logger.debug("json2 :{}",json);
            Map result =  transactionService.voidOrder(json);
            headers.add(status,"01");
           
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.OK);

        
        }catch(Exception e){
            logger.error("error msg8 : {}",e.getMessage());
            Map result =  new HashMap<>();
            headers.add(status,"02");
            headers.add(errorMsg,e.getMessage());
            result.put(status,"02");
            result.put(errorMsg,e.getMessage());
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } 


    @PostMapping("/payments/invoice")
    public ResponseEntity<String> paymentInvoices(HttpServletRequest request,@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(headersA, headersB);
        logger.info("[paymentInvoices][Controller] ");
        try{    
            logger.debug("json3 :{}",json);
            Map result =  transactionService.paymentInvoice(json);
            headers.add(status,"01");
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.OK);

        }catch(Exception e){
            logger.error("error msg9 : {}",e.getMessage());
            Map result =  new HashMap<>();
            headers.add(status,"02");
            headers.add(errorMsg,e.getMessage());
            result.put(status,"02");
            result.put(errorMsg,e.getMessage());
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/payments/void")
    public ResponseEntity<String> voidPaymentInvoices(HttpServletRequest request,@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(headersA, headersB);
        logger.info("[voidPaymentInvoices][Controller] ");
        try{    
            logger.debug("json4 :{}",json);
            headers.add(status,"01");
            Map result =  transactionService.voidPaymentInvoice(json);
           
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.OK);

       
        }catch(Exception e){
            logger.error("error msg10 : {}",e.getMessage());
            Map result =  new HashMap<>();
            headers.add(status,"02");
            headers.add(errorMsg,e.getMessage());
            result.put(status,"02");
            result.put(errorMsg,e.getMessage());
            return new ResponseEntity<>((new JSONSerializer().deepSerialize(result)),headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } 

    // =========  

}
