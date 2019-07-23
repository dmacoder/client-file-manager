package com.bellsoft.updater.api.v1.service.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bellsoft.updater.api.v1.dao.file.UpdaterDAO;
import com.bellsoft.updater.api.v1.domain.file.FimClientFileTb;
import com.bellsoft.updater.api.v1.repository.file.UpdaterRepository;
import com.bellsoft.updater.common.CustomParamMap;

@Service
public class UpdaterServiceImpl implements UpdaterService{

    @Autowired
    private UpdaterDAO updaterDAO;
    
    @Autowired
    private UpdaterRepository updaterRepository; 
    

    @Override
    public List<HashMap<String, Object>> getListClientFiles(CustomParamMap paramMap) {
        return updaterDAO.selectListClientFiles(paramMap);
    }
    @Override
    public int createFile(Map<String, Object> fileMap) {
        return updaterDAO.insertFile(fileMap);
        
    }
    @Override
    public HashMap<String, Object> getClientFileByFileId(String fileId) {
        return updaterDAO.selectClientFileByFileId(fileId);
    }
    @Override
    public Page<FimClientFileTb> findClientFileList(Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber() <=0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        return updaterRepository.findAll(pageable);
    }
    
   
    
}
