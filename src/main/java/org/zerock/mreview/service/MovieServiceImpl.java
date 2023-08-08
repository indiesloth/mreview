package org.zerock.mreview.service;

import java.util.*;
import java.util.function.Function;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.*;
import org.zerock.mreview.entity.*;
import org.zerock.mreview.repository.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

  private final MovieRepository movieRepository;

  private final MovieImageRepository imageRepository;

  @Transactional
  @Override
  public Long register(MovieDTO movieDTO) {

    Map<String, Object> entityMap = dtoToEntity(movieDTO);
    Movie movie = (Movie) entityMap.get("movie");
    List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

    movieRepository.save(movie);

    imageRepository.saveAll(movieImageList);

    return movie.getMno();
  }

  @Override
  public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {

    Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());

    Page<Object[]> result = movieRepository.getListPage(pageable);

    Function<Object[], MovieDTO> fn = (arr -> entitiesToDTO(
        (Movie) arr[0],
        (List<MovieImage>) (Arrays.asList((MovieImage)arr[1])),
        (Double) arr[2],
        (Long) arr[3])
    );
    return new PageResultDTO<>(result, fn);
  }

  @Override
  public MovieDTO getMovie(Long mno) {

    List<Object[]> result = movieRepository.getMovieWithAll(mno);

    Movie movie = (Movie) result.get(0)[0];

    List<MovieImage> movieImageList = new ArrayList<>();

    result.forEach(arr -> {
      MovieImage movieImage = (MovieImage) arr[1];
      movieImageList.add(movieImage);
    });

    Double avg = (Double) result.get(0)[2];
    Long reviewCnt = (Long) result.get(0)[3];

    return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
  }
}
