package org.zerock.mreview.dto;

import lombok.*;
import org.springframework.data.domain.*;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

  /*
    화면에서 전달되는 목록 관련된 데이터에 대한 DTO
   */
  private int page;
  private int size;
  private String type;
  private String keyword;

  public PageRequestDTO() {
    this.page = 1;
    this.size = 10;
  }

  public Pageable getPageable(Sort sort) {
    return PageRequest.of(page - 1, size, sort);
  }
}
