package org.zerock.mreview.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.zerock.mreview.entity.*;

public interface ReviewRepository extends AbstractRepository<Review, Long> {

  /*
    attributePaths : 로딩 설정을 변경하고 싶은 속성의 이름을 배열로 명시
    type : @EntityGraph를 어떤 방식으로 적용할 것인지 설정
    type의 FATCH 속성값은 attributePaths에 명시한 속성은 EAGER(즉시 로딩)로 처리, 나머지는 LAZY(지연 로딩)
    type의 LOAD 속성값은 attributePaths에 명시한 속성은 EAGER(즉시 로딩)로 처리, 나머지는 엔티티에 명시되거나 기본 방식
   */
  @EntityGraph(attributePaths = {"member"}, type = EntityGraphType.FETCH)
  List<Review> findByMovie(Movie movie);

  //@Modifying  //Query 에서 update 나 delete 사용하기 위해서 반드시 필요
  //@Query("delete from Review r where r.member = :member")
  /*
    FK 쪽을 먼저 삭제하도록 수정하고 메서드 선언부에  @Transactional 과
    @Commit을 추가하기 싫으면 위의 쿼리문을 사용하면 됨
   */

  void deleteByMember(Member member);
}
