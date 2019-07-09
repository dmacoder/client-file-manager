package com.bellsoft.updater.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bellsoft.updater.common.CustomParamMap;

public class UploadFileUtils {

	private static final Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);

	// public static String uploadFile(String uploadPath,
	// String originalName,
	// byte[] fileData)throws Exception{
	//
	// return null;
	// }
	//

	public static String checkFileType(File file) {

        try {
            String contentType = Files.probeContentType(file.toPath());
            
            logger.info("Upload ContentType : "+contentType);
            
            if(contentType == null) {
             return "F"; 
            }else if(contentType.startsWith("image")) {
             return "I";   
            }else if(contentType.startsWith("audio")) {
                return "A";
            }else if(contentType.startsWith("video")) {
                return "V";
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "F";
    }
	
	/**
	 * @method name : uploadClientFile
	 * @date : 2018. 12. 20. 오후 5:59:04
	 * @author :  [](psc)
	 * @history: 
	 * <pre>
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용  
	 *	----------- ------------------- ---------------------------------------
	 *	2018. 12. 20.		psc				최초 작성 
	 *	-----------------------------------------------------------------------
	 * </pre>
	 * @description: 클라이언트 파일 업로드
	 * <pre>
	 * 1. 개요 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @param uploadPath
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> uploadClientFile(String uploadPath, CustomParamMap paramMap, MultipartHttpServletRequest request)
	        throws IllegalStateException,
            IOException {
	    
		String uid = UUID.randomUUID().toString().replaceAll("-", "");
		
		
		Map<String, MultipartFile> fileMap = request.getFileMap();
		
		String sourceFilename = null;
		
		String sourceFileExtension = null;
		
		String uploadedFilename = null;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> listMap = null;

		//String savedPath = calcPath(uploadPath);
       
		String fileVersion = paramMap.get("fileVersion").toString();
		
		logger.info("파일 버전 : "+fileVersion);
		
		 for (MultipartFile file : fileMap.values()) {
	
			if (file.isEmpty() == false) {
				sourceFilename = file.getOriginalFilename();
				sourceFileExtension = sourceFilename.substring(sourceFilename.lastIndexOf("."));
				uploadedFilename = uid + sourceFileExtension;
				String savedPath = calcPath(uploadPath);
				 
				 File saveFile = new File(uploadPath + savedPath, uploadedFilename);
				//file = new File(uploadPath+uploadedFilename);
				
				if (saveFile.exists() == false) {
		            saveFile.mkdirs();
		        }
				
				file.transferTo(saveFile);
				
				listMap = new HashMap<String, Object>();
				
				logger.info("absolutepath : "+saveFile.getAbsolutePath());
				
				logger.info("파일 다운로드 경로 : "+saveFile.getPath());
				
				logger.info("파일 이름 : "+sourceFilename);
                
				String fileHash = getFileHash(saveFile.getAbsolutePath(),"SHA-256");
				
				logger.info("파일 해쉬 : "+fileHash);
				
				listMap.put("fileNm", sourceFilename);
                listMap.put("fileDownloadPath", savedPath+File.separator+uploadedFilename);
                listMap.put("fileSize", file.getSize());
                
                listMap.put("fileVersion", fileVersion);
                
                listMap.put("fileHash", fileHash);
                
				listMap.put("regId", paramMap.get("regId"));
				listMap.put("modId", paramMap.get("modId"));
				//listMap.put("publicFl", paramMap.get("publicFl"));
				 
				list.add(listMap);
			}
		}
		return list;

	}
	
	public static String getFileHash(String fileNm,String algorithm) {
	    
	    String fileHash = null;
	    
	    try {
	        fileHash =  FileUtils.getFileHash(fileNm, algorithm);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    
	    return fileHash;
	}
	
	private static String calcPath(String uploadPath) {
	    
	    // /2018/11/12
	    
		Calendar cal = Calendar.getInstance();

		String yearPath = File.separator + cal.get(Calendar.YEAR);

		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);

		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

		makeDir(uploadPath, yearPath, monthPath, datePath);

		logger.info(datePath);

		return datePath;
	}

	private static boolean deleteFile(File file) {
	    
	    boolean isDelete = false;
	    
	    if( file.exists() ){ //파일존재여부확인
            
            if(file.isDirectory()){ //파일이 디렉토리인지 확인
                 
                File[] files = file.listFiles();
                 
                for( int i=0; i<files.length; i++){
                    if( files[i].delete() ){
                        logger.info(files[i].getName()+"디렉토리 내 하위 파일 삭제 성공");
                    }else{
                        logger.info(files[i].getName()+"디렉토리 내 하위 파일 삭제 실패");
                    }
                }
                 
            }
            
            if(file.delete()){
                logger.info("파일삭제 성공");
                isDelete = true;
            }else{
                logger.info("파일삭제 실패");
            }
             
        }else{
            isDelete = true;
            logger.info("파일이 존재하지 않습니다.");
        }
	    
	    return isDelete;
	}
	
	private static void makeDir(String uploadPath, String... paths) {

		if (new File(paths[paths.length - 1]).exists()) {
			return;
		}

		for (String path : paths) {

			File dirPath = new File(uploadPath + path);

			if (!dirPath.exists()) {
				dirPath.mkdir();
			}
		}
	}

    public static List<Map<String, Object>> uploadClientFile2(String uploadPath, CustomParamMap paramMap,  MultipartFile file)
            throws IllegalStateException,
            IOException {
        
        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        
        
        String sourceFilename = null;
        
        String sourceFileExtension = null;
        
        String uploadedFilename = null;

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> listMap = null;

        //String savedPath = calcPath(uploadPath);
       
        String fileVersion = paramMap.get("fileVersion").toString();
        
        logger.info("파일 버전 : "+fileVersion);
        
            if (file.isEmpty() == false) {
                sourceFilename = file.getOriginalFilename();
                sourceFileExtension = sourceFilename.substring(sourceFilename.lastIndexOf("."));
                uploadedFilename = uid + sourceFileExtension;
                String savedPath = calcPath(uploadPath);
                 
                 File saveFile = new File(uploadPath + savedPath, uploadedFilename);
                //file = new File(uploadPath+uploadedFilename);
                
                if (saveFile.exists() == false) {
                    saveFile.mkdirs();
                }
                
                file.transferTo(saveFile);
                
                listMap = new HashMap<String, Object>();
                
                logger.info("absolutepath : "+saveFile.getAbsolutePath());
                
                logger.info("파일 다운로드 경로 : "+saveFile.getPath());
                
                logger.info("파일 이름 : "+sourceFilename);
                
                String fileHash = getFileHash(saveFile.getAbsolutePath(),"SHA-256");
                
                logger.info("파일 해쉬 : "+fileHash);
                
                listMap.put("fileNm", sourceFilename);
                listMap.put("fileDownloadPath", savedPath+File.separator+uploadedFilename);
                listMap.put("fileSize", file.getSize());
                
                listMap.put("fileVersion", fileVersion);
                
                listMap.put("fileHash", fileHash);
                
                listMap.put("regId", paramMap.get("regId"));
                listMap.put("modId", paramMap.get("modId"));
                //listMap.put("publicFl", paramMap.get("publicFl"));
                 
                list.add(listMap);
            }
        
        return list;
    }
}
