package com.bellsoft.updater.api.v1.controller.common;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bellsoft.updater.api.v1.service.common.CommonService;

@RestController("ApiV1CommonRestController")
@RequestMapping("api/v1")
public class ApiCommonRestController {
    
    @Autowired
    CommonService commonService;
    
    private static final Logger log = LoggerFactory.getLogger(ApiCommonRestController.class);
    
    @RequestMapping(value="/primary-keys/{pkCd}", method=RequestMethod.POST)
    public ResponseEntity<?> createPrimaryKey(@PathVariable("pkCd") String pkCd){
        ResponseEntity<?> entity = null;
        HashMap<String, Object> result = new HashMap<String, Object>();
        
        try {
            
            result.put("pkCd", pkCd);
            // 프로시저 호출시에는 요청한 객체에 자동으로 맵핑됨..
            commonService.createPrimaryKey(result);
            entity = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        
        return entity;
        
    }
    
    
}
