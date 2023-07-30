package org.zerock.mreview.dto;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.*;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {

  private String fileName;
  private String uuid;
  private String folderPath;

  public String getImageURL() {
    /*
      try {
        return URLEncoder.encode(folderPath + File.separator + fileName, StandardCharsets.UTF_8);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      책에서는 상단의 내용으로 적혀있지만 Exception이 발생하지 않는다고 나오므로 바로 리턴
     */
    return URLEncoder.encode(folderPath + File.separator + fileName, StandardCharsets.UTF_8);
  }

}
