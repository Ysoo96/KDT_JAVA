package com.javateam.springFileuploadDragDrop.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javateam.springFileuploadDragDrop.util.FileNamingEnDecodingUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

	@Autowired
	FileNamingEnDecodingUtil fnedUtil;

	@Value("${fileUpload.path}")
	// application.properties 파일의 imageUpload.path=D:/lsh/upload/image/
	private String fileUploadPath;

	@RequestMapping("/")
	public String home() {

		log.info("##### home ######");
		return "redirect:/fileUpload";
	}

	@RequestMapping(value = "/fileUpload", method = RequestMethod.GET)
	public String dragAndDrop(Model model) {

		log.info("##### fileUpload ######");
		return "fileUpload";
	}

	@RequestMapping(value = "/fileUpload/post")
	@ResponseBody
	public String upload(MultipartHttpServletRequest multipartRequest) {

		log.info("##### /fileUpload/post ######");
		
		String msg = ""; // 파일 업로드 이후 메시지
		
		// JSON 메시지 생성
		String result = "";
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		
		/////////////////////////////////////////////////////////////////////////////////

		Iterator<String> itr = multipartRequest.getFileNames();

		while (itr.hasNext()) {

			MultipartFile mpf = multipartRequest.getFile(itr.next());

			String originalFilename = mpf.getOriginalFilename(); // 파일명

			// 원래 파일명 암호화
			String encodedFileName = fnedUtil.encodeFilename(originalFilename);

			String fileFullPath = fileUploadPath + "/" + encodedFileName; // 파일 전체 경로

			try {
				
				mpf.transferTo(new File(fileFullPath)); // 파일저장 실제로는 service에서 처리

				log.info("originalFilename : " + originalFilename);
				log.info("encodedFileName : " + encodedFileName);
				log.info("fileFullPath : " + fileFullPath);
				
				msg = "파일 업로드에 성공하였습니다.";
				map.put("encodedFileName", encodedFileName);
				map.put("originalFilename", originalFilename);
				
			} catch (Exception e) {
				log.error("파일 업로드 에러 : " + fileFullPath);
				e.printStackTrace();
				
				msg = "파일 업로드에 실패하였습니다.";
			} // try

		} // while

		////////////////////////////////////////////////////////////////////////////
		
		map.put("msg", msg);
				
		try {
			result = mapper.writeValueAsString(map);
			
		} catch (JsonProcessingException e) {
			log.error("JSON 정보 생성 실패 : ");
			e.printStackTrace();
		}
		
		return result;
	} //

}