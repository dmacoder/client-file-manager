package com.bellsoft.updater.api.v1.service.common;

import java.util.HashMap;

import org.springframework.stereotype.Service;

public interface CommonService {

    HashMap<String, Object> createPrimaryKey(HashMap<String, Object> pk);
}

