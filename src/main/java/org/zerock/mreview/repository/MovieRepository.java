package org.zerock.mreview.repository;

import java.util.List;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Movie;

public interface MovieRepository extends AbstractRepository<Movie, Long> {

  @Query("select m, mi, avg(coalesce(r.grade, 0)), count(distinct r) from Movie m "
      + " left join MovieImage mi on mi.movie = m "
      + " left join Review r on r.movie = m group by m")
  Page<Object[]> getListPage(Pageable pageable);  //페이지 처리

  @Query("select m, mi from Movie m "
      + " left outer join MovieImage mi on mi.movie = m "
      + " where m.mno = :mno")
  List<Object[]> getMovieWithAll(Long mno); //특정 영화 조회
}
