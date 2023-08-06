package org.zerock.mreview.service;

import java.util.*;
import java.util.stream.Collectors;
import org.zerock.mreview.dto.*;
import org.zerock.mreview.entity.*;

public interface MovieService {

  Long register(MovieDTO movieDTO);

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
