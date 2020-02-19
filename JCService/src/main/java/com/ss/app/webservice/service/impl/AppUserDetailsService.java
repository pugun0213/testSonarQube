package com.ss.app.webservice.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

import java.security.MessageDigest;
import com.ss.app.webservice.repository.custom.ParameterRepositoryCustom;
import java.util.Map;
import java.util.HashMap;

@Component
public class AppUserDetailsService implements UserDetailsService {
    
    private static Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);


    @Autowired
    ParameterRepositoryCustom parameterRepository;



    @Override
    public UserDetails loadUserByUsername(String userId) {
        try{




            Map authToken  =new HashMap<>();



                authToken.put("user","jpapi");
                authToken.put("password","g0,kiNm$s3rv1ce");
                authToken.put("role","CLIENT");

            if( !userId.equalsIgnoreCase(  authToken.get("user").toString() )  ){
                throw new UsernameNotFoundException(String.format("The userId %s doesn't exist", userId));
            }
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(  authToken.get("role").toString() ));
            String encodePassword = AppUserDetailsService.encodeSha256(authToken.get("password").toString());
            String r=authToken.get("user").toString();

            return  new org.springframework.security.core.userdetails.User(r, encodePassword , authorities);
        }catch(Exception e){
            logger.error("error msg:{}",e);

            throw new RuntimeException(e);
        }
        
    }

    public static String encodeSha256(String message){
        logger.info("encode plaintext");
        try{
          MessageDigest md = MessageDigest.getInstance("SHA-256");
          md.update(message.getBytes());
          String hash = bytesToHex(md.digest());
          logger.debug("get hashmessage ");
          return hash;
        }catch(Exception e){
            logger.error("error msg:{}",e);

            throw new RuntimeException(e);
        }
    } 

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}