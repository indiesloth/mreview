package org.zerock.mreview.controller;

import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Log4j2
public class UploadTestController {

  @GetMapping("/uploadEx")
  public void uploadEx() {

  }

  @GetMapping("/uploadEEx")
  public void uploadEEx() {

  }

  @PostMapping("/uploadEEx")
  public void uploadEExPost(@RequestParam("files") MultipartFile[] files) throws IOException {
    if(files != null) {
      for (MultipartFile file : files) {
        log.info("fileName : " + file.getOriginalFilename());
      }
    }
  }
}
