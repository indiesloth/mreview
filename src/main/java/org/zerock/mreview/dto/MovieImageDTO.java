package org.zerock.mreview.dto;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieImageDTO {

  private String uuid;

  private String imgName;

  private String path;

  public String getImage() {
    return URLEncoder.encode(path + File.separator + uuid + "_" + imgName, StandardCharsets.UTF_8);
  }

  public String getThumbnailURL() {
    return URLEncoder.encode(path + File.separator + "s_" + uuid + "_" + imgName,
        StandardCharsets.UTF_8);
  }

}
