package com.bellsoft.updater.api.v1.controller.file;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bellsoft.updater.api.v1.exception.FileIdNotFoundException;
import com.bellsoft.updater.api.v1.service.common.CommonService;
import com.bellsoft.updater.api.v1.service.file.UpdaterService;
import com.bellsoft.updater.common.CustomParamMap;
import com.bellsoft.updater.common.util.InetUtils;
import com.bellsoft.updater.common.util.UploadFileUtils;

import lombok.extern.slf4j.Slf4j;

@RequestMapping(value = "api/v1")
@RestController
@Slf4j
public class UpdaterRestController {

    @Autowired
    UpdaterService updaterService;

    @Autowired
    CommonService commonService;

    @Value("${client.file.path}")
    private String uploadPath;

    @RequestMapping(value = "/client-files", method = RequestMethod.GET)
    public ResponseEntity<?> getListClientFiles(CustomParamMap paramMap) {

        ResponseEntity<?> entity = null;
        Map<String, Object> result = new HashMap<String, Object>();

        // String -> int 타입으로 저장 mybatis에서 파라미터로 받을때 sql injection 방지하기 위해 $를 쓰지않고 #을 쓰기 때문
        if (paramMap.containsKey("offset")) {
            int offset = Integer.parseInt((String) paramMap.get("offset"));
            paramMap.put("offset", offset);
        }
        if (paramMap.containsKey("limit")) {
            int limit = Integer.parseInt((String) paramMap.get("limit"));
            paramMap.put("limit", limit);
        }

        try {
            List<HashMap<String, Object>> list = updaterService.getListClientFiles(paramMap);
            if (list == null || list.isEmpty()) {
                log.info("File List not found");
                // no content 또는 not found
                // entity = list.size() > 0 ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);

                throw new FileIdNotFoundException("Client File List Not Found", "클라이언트 파일 업데이트 리스트가 존재하지 않습니다.");

            } else {
                result.put("data", list);

                entity = new ResponseEntity<>(result, HttpStatus.OK);
            }
        } catch (FileIdNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return entity;

    }

    @RequestMapping(value = "/client-files", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<?> uploadClientFiles(@RequestPart("file") MultipartFile file, MultipartHttpServletRequest req, HttpServletResponse res, HttpServletRequest hreq, Principal principal) {

        CustomParamMap paramMap = new CustomParamMap();

        paramMap.put("fileVersion", hreq.getParameter("fileVersion"));

        ResponseEntity<?> entity = null;
        HashMap<String, Object> result = new HashMap<String, Object>();

        paramMap.put("regId", principal.getName());
        paramMap.put("modId", principal.getName());
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        List<Map<String, Object>> uploadMapList;

        try {

            log.info("=====================업로드 시작=======================");

            // 업로드 후에 list에 filehash,filesize 저장
            // uploadMapList = UploadFileUtils.uploadClientFile(uploadPath, paramMap, req);

            uploadMapList = UploadFileUtils.uploadClientFile2(uploadPath, paramMap, file);

            log.info("=====================업로드 완료=======================");
            log.info("=====================업로드 리스트 사이즈 : " + uploadMapList.size() + "=======================");

            // UploadFileUtils.uploadFile2(uploadPathTicket,paramMap,req);
            for (int i = 0, size = uploadMapList.size(); i < size; i++) {
                // 파일 업로드시 외래키 값으로 조회하여 기존에 파일히스토리가 존재하면 무조건 삭제여부를 Y 시켜주는 로직추가
                // 티켓ID(외래키값)+저장 파일 이름 두가지 조건으로 조회하여 삭제해야함 티켓ID에 여러 이미지가 존재하기 때문에~
                Map<String, Object> fileMap = uploadMapList.get(i);

                HashMap<String, Object> pk = new HashMap<String, Object>();
                pk.put("pkCd", "FC");
                commonService.createPrimaryKey(pk);
                String fileIdPk = (String) pk.get("outParam01");

                fileMap.put("fileId", fileIdPk);

                
                fileMap.put("ipAddress", InetUtils.getIpAddress(request));

                updaterService.createFile(fileMap);

                log.info("fileDownloadPath : " + fileMap.get("fileDownloadPath"));

                result.put("fileDownloadPath", fileMap.get("fileDownloadPath"));

                entity = new ResponseEntity<>(result, HttpStatus.OK);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.info("=====================업로드 에러=======================");
            entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            e.printStackTrace();
        }

        return entity;

    }

    @RequestMapping(value = "/client-files/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<?> getClientFileByFileId(@PathVariable("fileId") String fileId) {

        ResponseEntity<?> entity = null;
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            HashMap<String, Object> map = updaterService.getClientFileByFileId(fileId);
            if (map == null || map.isEmpty()) {
                log.info("FileId not found");
                // no content 또는 not found
                // entity = list.size() > 0 ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);

                throw new FileIdNotFoundException("Client FileId Not Found", "클라이언트 fileId : " + fileId + "가 존재하지 않습니다.");

            } else {
                result.put("data", map);

                entity = new ResponseEntity<>(result, HttpStatus.OK);
            }
        } catch (FileIdNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return entity;

    }

    @RequestMapping(value = "/client-files/downloads/{fileId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, @PathVariable("fileId") String fileId) {

        
        HashMap<String, Object> map = updaterService.getClientFileByFileId(fileId);
        
        if (map == null || map.isEmpty()) {
            throw new FileIdNotFoundException("Client FileId Not Found", "클라이언트 fileId : " + fileId + "가 존재하지 않습니다.");
        }
        
        //파일 다운로드 Full Path
        String fileDownloadFullPath = map.get("fileDownloadPath").toString();
        
        //원본 파일명
        String sourceFileName =  map.get("fileNm").toString();
        
        Resource resource = new FileSystemResource(uploadPath + fileDownloadFullPath);

        if (resource.exists() == false) {
            log.info("해당 파일이 없습니다.\n경로 : " + uploadPath + fileDownloadFullPath);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //String resourceName = resource.getFilename();

        HttpHeaders headers = new HttpHeaders();
        try {

            boolean checkIE = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1);

            /*
             * String downloadName = null;
             * 
             * if (checkIE) { downloadName = URLEncoder.encode(resourceOriginalName, "UTF8").replaceAll("\\+", " "); } else { downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1"); }
             */

            String downloadName = sourceFileName;

            if (checkIE) {
                downloadName = URLEncoder.encode(sourceFileName, "UTF8").replaceAll("\\+", " ");
            } else {
                downloadName = new String(sourceFileName.getBytes("UTF-8"), "ISO-8859-1");
            }

            // sourceFileName = URLEncoder.encode(sourceFileName, "UTF-8");

            headers.add("Content-Disposition", "attachment; filename=" + downloadName);

            // headers.add("Content-Transfer-Encoding", "binary");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }
}
