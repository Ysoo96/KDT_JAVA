package com.javateam.springFileupload.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.javateam.springFileupload.domain.FileUploadForm;
import com.javateam.springFileupload.util.FileNamingEnDecodingUtil;

import net.coobird.thumbnailator.Thumbnails;

@Controller
public class FileUploadController {

	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
	
	@Autowired
	FileNamingEnDecodingUtil fnedUtil;

	@Value("${imageUpload.path}") 
	// application.properties 파일의 imageUpload.path=D:/lsh/upload/image/
	private String imageUploadPath; 

	@GetMapping(value = {"/", "/show"})
	public String form() {

		log.info("form");
		return "file_upload_form";
	}

	@PostMapping("/save")
	public String save(@ModelAttribute("uploadForm") FileUploadForm uploadForm, Model model, BindingResult result) {
		
		log.info("save");

		List<MultipartFile> files = uploadForm.getFiles();
		List<String> fileNames = new ArrayList<String>(); // 파일(들)명(인자 전송용)
		
		// 파일(들)명(인자 전송용 : 암호화)
		List<String> encodedFiles = new ArrayList<>(); 

		// 썸네일(thumbnail picture) 파일(인자 전송용)
		List<String> thumbFileNames = new ArrayList<>();
		
		// 썸네일(thumbnail picture) 파일(인자 전송용) : 암호화 적용
		List<String> encodedThumbFileNames = new ArrayList<>();

		// 오류(에러) 처리
		if (result.hasErrors()) {

			for (ObjectError error : result.getAllErrors()) {
				System.err.println("Error: " + error.getCode() + " - " + error.getDefaultMessage());
			}
			
			// return "/show";
			return "file_upload_form";
		}

		log.info("파일 갯수 : " + files.size());

		if (!uploadForm.getFiles().isEmpty() && files != null && files.size() > 0) { // 파일(들) 유효성 점검

			String tmpFileName = "";
			String tmpFileExt = "";

			// 파일 정보
			for (int i = 0; i < files.size(); i++) {

				// 파일명
				tmpFileName = uploadForm.getFiles().get(i).getOriginalFilename();
				// 확장자
				tmpFileExt = tmpFileName.substring(tmpFileName.lastIndexOf(".") + 1, tmpFileName.length());

				log.info("파일명 - 확장자 :  " + tmpFileName + "-" + tmpFileExt);

			} // for

			// 업로드 파일 확장자 제한 : 그림 파일 업로드 게시판용
			if (tmpFileExt.equalsIgnoreCase("JPG") || tmpFileExt.equalsIgnoreCase("JPEG")
					|| tmpFileExt.equalsIgnoreCase("JFIF") || tmpFileExt.equalsIgnoreCase("PNG")) {

				log.info("그림 파일 확장자 OK !");

				// if (files != null && files.size() > 0) {

				// FileOutputStream fos = null;

				for (MultipartFile multipartFile : files) {

					String fileName = multipartFile.getOriginalFilename();
					fileNames.add(fileName);

					// 파일 저장소(d:\\lsh\\upload\\image) 에 저장
					try {
						// byte[] bytes = uploadForm.getFiles().get(i).getBytes();
						byte[] bytes = multipartFile.getBytes();

						// 원래 파일명 암호화
						String encodedFileName = fnedUtil.encodeFilename(fileName);
						File outFileName = new File(imageUploadPath + encodedFileName);
						encodedFiles.add(encodedFileName);

						// 썸네일 파일명 생성
						// 썸네일(thumbnail) path : webp/gif를 제외한 파일들은 PNG 형식으로 저장
						// ex) d:\\lsh\\upload\\image\\thumbnail

						String thumbPath = imageUploadPath + "thumbnail/";			
						
						// String thumbPathFileName = "thumb_" + fileName.split("\\.")[0] + ".png";
						
						// //////////////////////////////////////////////////////////////////////
						//
						// 파일명 암호화 적용
						String thumbPathFileName = "thumb_" + fnedUtil.encodeFilename(fileName).split("\\.")[0] + ".png";

						// 파일 업로드
						// fos = new FileOutputStream(outFileName);
						// fos.write(bytes);
						
						FileCopyUtils.copy(bytes, outFileName);

						// 썸네일 파일 저장 시작
						// https://github.com/coobird/thumbnailator
						// 200*100 크기의 썸네일 작성

						File thumbnail = new File(thumbPathFileName);
						
						// if (outFileName.exists()) {
						// thumbnail.getParentFile().mkdirs();
						
						Thumbnails.of(outFileName).size(200, 100).outputFormat("png").toFile(thumbPath + thumbnail);
						// }
						// 썸네일 파일 저장 끝

						// 썸네일 파일 인자 저장
						
						///////////////////////////////////////////////////////////////////
						// 
						// 파일명 복호화 적용
						encodedThumbFileNames.add(fnedUtil.decodeFilename(thumbPathFileName, "png"));

						thumbFileNames.add(thumbPathFileName);

					} catch (IOException e) {
						
						log.info("FileUploadController : 파일 저장 오류 !");
						e.printStackTrace();
						
					} finally {
//						try {
//							if (fos != null) {
//								fos.close();
//							}	
//						} catch (IOException e) {
//							log.info("FileUploadController save IOE : ");
//							e.printStackTrace();
//						}
						log.info("파일 업로드 성공 !");
					} // try

				} // for

				// if : 그림 파일 점검
			} else {

				log.info("변환이 지원되지 않는 파일이거나 올바른 그림 파일 형식이 아닙니다.");
				model.addAttribute("err_msg", "변환이 지원되지 않는 파일이거나 올바른 그림 파일 형식이 아닙니다.");
				return "/error/image_error"; // 에러 처리 페이지로 이동
			} //

		} else {
			log.error("파일 타입 에러 ! ");
		}

		// 전송 인자(원(original) 이미지, 썸네일 이미지)
		model.addAttribute("files", fileNames);
		model.addAttribute("thumbFiles", thumbFileNames);
		
		// 암호화된 파일명
		model.addAttribute("encodedthumbFiles", encodedThumbFileNames);
		model.addAttribute("encodedFiles", encodedFiles);

		return "file_upload_success";
	} //

}
