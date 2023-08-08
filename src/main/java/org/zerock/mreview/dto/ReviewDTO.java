package org.zerock.mreview.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

  private Long reviewnum;

  private Long mno;

  private Long mid;
  private String nickName;
  private String email;
  private int grade;
  private String text;
  private LocalDateTime regDate, modDate;
}
