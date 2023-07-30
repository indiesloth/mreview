package org.zerock.mreview.repository;

import java.util.*;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Commit;
import org.zerock.mreview.entity.*;

@SpringBootTest
class MovieRepositoryTests {

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private MovieImageRepository imageRepository;

  @Commit
  @Transactional
  @Test
  public void insertMovies() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Movie movie = Movie.builder().title("Movie....." + i).build();

      System.out.println("---------------------------------------");

      movieRepository.save(movie);

      int count = (int) ((Math.random() * 5) + 1);  //1,2,3,4,5

      for (int j = 0; j < count; j++) {
        MovieImage movieImage = MovieImage.builder()
            .uuid(UUID.randomUUID().toString())
            .movie(movie)
            .imgName("test" + j + ".jpg")
            .build();
        imageRepository.save(movieImage);
      }
      System.out.println("---------------------------------------");
    });
  }

  @Test
  public void testListPage() {
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Direction.DESC, "mno"));

    Page<Object[]> result = movieRepository.getListPage(pageRequest);

    for (Object[] objects : result.getContent()) {
      System.out.println(Arrays.toString(objects));
    }
  }

  @Test
  public void testGetMovieWithAll() {
    List<Object[]> result = movieRepository.getMovieWithAll(94L);

    System.out.println(result);

    for (Object[] arr : result) {
      System.out.println(Arrays.toString(arr));
    }
  }
}