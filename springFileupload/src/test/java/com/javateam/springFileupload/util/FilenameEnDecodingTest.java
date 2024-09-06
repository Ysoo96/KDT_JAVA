package com.javateam.springFileupload.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FilenameEnDecodingTest {
	
	private static final Logger log = LoggerFactory.getLogger(FilenameEnDecodingTest.class);
	
	@Autowired
	FileNamingEnDecodingUtil fnedUtil;

	@Test
	public void test() {
		
		log.info("파일명 인코딩 테스트");
		
		String encodingFilename = fnedUtil.encodeFilename("테스트_그림파일_1.jpg");
		
		log.info("인코딩된 파일 : {}", encodingFilename); // ex) 테스트_그림파일_1_ae0738ab5e434b77837f1d01baed8444.jpg
		log.info("디코딩된 파일 : {}", fnedUtil.decodeFilename(encodingFilename, "jpg")); // ex) 테스트_그림파일_1.jpg
	} //
	
}