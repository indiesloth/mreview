package org.zerock.mreview.dto;

import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

  private Long mno;

  private String title;

  /*
    빌더 패턴을 통해 인스턴스를 만들 때 특정 필드를 특정 값으로 초기화하고 싶다면 @Builder.Default를 쓰면 된다
   */
  @Builder.Default
  private List<MovieImageDTO> imageDTOList = new ArrayList<>();

  //영화의 평균 평점
  private double avg;

  //리뷰 수 jpa의 count()
  private int reviewCnt;

  private LocalDateTime regDate;

  private LocalDateTime modDate;
}
