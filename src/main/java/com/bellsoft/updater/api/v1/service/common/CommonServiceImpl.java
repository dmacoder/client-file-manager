package com.bellsoft.updater.api.v1.service.common;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bellsoft.updater.api.v1.dao.common.CommonDAO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService{
    
    @Autowired
    CommonDAO commonDAO;
    
    
    @Override
    public HashMap<String, Object> createPrimaryKey(HashMap<String, Object> pk) {
        return commonDAO.callProcInsertPk(pk);
    }
    
    
}
