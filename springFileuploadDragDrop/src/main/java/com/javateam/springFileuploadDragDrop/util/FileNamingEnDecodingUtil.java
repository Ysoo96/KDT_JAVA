package com.javateam.springFileuploadDragDrop.util;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileNamingEnDecodingUtil {
	
	private static final Logger log = LoggerFactory.getLogger(FileNamingEnDecodingUtil.class);
	
	public String encodeFilename(String originalFilename) {
		
		log.info("파일명 인코딩 : UUID");
		
		String encodeFilename = "";
		
		String genId = UUID.randomUUID().toString();
        genId = genId.replace("-", "");
        
        String fileExtension = getExtension(originalFilename);
        
        // ex) 테스트_그림파일_1_1fdff3a109bd4de09699d4ed7a92e32c.jpg
        encodeFilename = originalFilename.split("\\.")[0] + "_" + genId + "." + fileExtension;
        
        return encodeFilename;
	}
	
	public String decodeFilename(String encodedFilename, String fileExtension) {
		
		log.info("파일명 디코딩 : UUID");
		
		// ex) 테스트_그림파일_1_1fdff3a109bd4de09699d4ed7a92e32c.jpg
		
		// 마지막 "_" 에서 분리 → 원래의 파일명 추출
        return encodedFilename.substring(0, encodedFilename.lastIndexOf("_"))  + "." + fileExtension;
	}
	
	/**
     * 파일명으로부터 확장자 추출(반환)
     * 
     * @param fileName 확장자 포함한 파일명
     * @return 파일명에서 확장자 이름만 반환
     */
    public static String getExtension(String fileName) {
    	
        int dotPosition = fileName.lastIndexOf('.');
        
        if (dotPosition != -1  && fileName.length() - 1 > dotPosition) {
            return fileName.substring(dotPosition + 1);
        } else {
            return "";
        }
    }

}