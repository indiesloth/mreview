package org.zerock.mreview.controller;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;

@RestController
@Log4j2
public class UploadController {
  /*
    파일을 저장하는 단계에서 고려할 사항
    - 업로드된 확장자가 이미지만 가능하도록 검사 (첨부파일을 이용한 원격 셀)
    - 동일한 이름의 파일이 업로드 된다면 기존 파일을 덮어쓰는 문제
    - 업로드된 파일을 저장하는 폴더의 용량 (동일한 폴더에 너무 많은 파일)
   */

  @Value("${org.zerock.upload.path}") //application.properties의 변수
  private String uploadPath;

  @PostMapping("/uploadAjax")
  public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {

    List<UploadResultDTO> resultDTOList = new ArrayList<>();

    for (MultipartFile uploadFile : uploadFiles) {
      //이미지 파일만 업로드 가능
      if (!uploadFile.getContentType().startsWith("image")) {
        log.warn("this file is not image type");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        //403(Forbidden, 금지됨): 서버가 요청을 거부하고 있다.
      }

      //실제 파일 이름 IE나 Edge 는 전체 경로가 들어오므로
      String originalName = uploadFile.getOriginalFilename();

      //String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
      String fileName = Objects.requireNonNull(originalName)
          .substring(originalName.lastIndexOf("\\") + 1);

      log.info("fileName : " + fileName);

      //날짜 폴더 생성
      String folderPath = makeFolder();

      //UUID
      String uuid = UUID.randomUUID().toString();

      //저장할 파일 이름 중간에 "_"를 이용해서 구분
      String saveName =
          uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

      Path savePath = Paths.get(saveName);

      try {
        //원본 파일 저장
        uploadFile.transferTo(savePath);

        //썸네일 생성
        String thumbnailSaveName =
            uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_"
                + fileName;

        //썸네일 파일 이름은 중간에 s_로 시작하도록
        File thumbnailFile = new File(thumbnailSaveName);

        //썸네일 생성
        Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

        resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
      } catch (IOException e) {
        e.printStackTrace();
      }

    }// end for
    return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
  }

  private String makeFolder() {
    String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    log.info("makeFolder str : " + str);

    /*
      File.separator - 프로그램이 실행 중인 OS에 해당하는 구분자를 리턴
      Window - /
      Linux - \
     */
    String folderPath = str.replace("//", File.separator);
    log.info("makeFolder folderPath : " + folderPath);

    //make folder
    File uploadPathFolder = new File(uploadPath, folderPath);

    if (!uploadPathFolder.exists()) {
      boolean mkdirsResult = uploadPathFolder.mkdirs();
      log.info("mkdirsResult : " + mkdirsResult);
    }
    return folderPath;
  }

  @GetMapping("/display")
  public ResponseEntity<byte[]> getFile(String fileName) {

    ResponseEntity<byte[]> result = null;

    try {
      String srcFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

      log.info("fileName : " + srcFileName);

      File file = new File(uploadPath + File.separator + srcFileName);

      log.info("file : " + file);

      HttpHeaders header = new HttpHeaders();

      //MIME타입 처리 (파일의 확장자에 따라서 브라우저에 전송하는 MIME 타입이 달라져야 하는 문제)
      header.add("Content-Type", Files.probeContentType(file.toPath()));

      //파일 데이터 처리
      result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return result;
  }

  @PostMapping("/removeFile")
  public ResponseEntity<Boolean> removeFile(String fileName) {

    String srcFileName = null;
    try {
      //원본 파일 삭제
      srcFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
      File file = new File(uploadPath + File.separator + srcFileName);
      boolean result = file.delete();

      //썸네일 파일 삭제
      File thumbnail = new File(file.getParent(), "s_" + file.getName());
      result = thumbnail.delete();
      return new ResponseEntity<>(result, HttpStatus.OK);
    }catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
