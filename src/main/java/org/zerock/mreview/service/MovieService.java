package org.zerock.mreview.service;

import java.util.*;
import java.util.stream.Collectors;
import org.zerock.mreview.dto.*;
import org.zerock.mreview.entity.*;

public interface MovieService {

  Long register(MovieDTO movieDTO);

  PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO);

  MovieDTO getMovie(Long mno);

  default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages, Double avg,
      Long reviewCnt) {
    MovieDTO movieDTO = MovieDTO.builder()
        .mno(movie.getMno())
        .title(movie.getTitle())
        .regDate(movie.getRegDate())
        .modDate(movie.getModDate())
        .build();

    List<MovieImageDTO> movieImageDTOList = movieImages.stream().map(movieImage -> {
      return MovieImageDTO.builder()
          .imgName(movieImage.getImgName())
          .path(movieImage.getPath())
          .uuid(movieImage.getUuid())
          .build();
    }).collect(Collectors.toList());

    movieDTO.setImageDTOList(movieImageDTOList);
    movieDTO.setAvg(avg);
    movieDTO.setReviewCnt(reviewCnt.intValue());

    return movieDTO;
  }

  default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {

    Map<String, Object> entityMap = new HashMap<>();

    //DTO -> Entity
    Movie movie = Movie.builder()
        .mno(movieDTO.getMno())
        .title(movieDTO.getTitle())
        .build();

    entityMap.put("movie", movie);

    List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

    //MovieImageDTO 처리
    if (imageDTOList != null && !imageDTOList.isEmpty()) {
      List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO -> {
        return MovieImage.builder()
            .path(movieImageDTO.getPath())
            .imgName(movieImageDTO.getImgName())
            .uuid(movieImageDTO.getUuid())
            .movie(movie)
            .build();
      }).collect(Collectors.toList());

      entityMap.put("imgList", movieImageList);
    }
    return entityMap;
  }
}
