package org.zerock.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.dto.*;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.entity.Review;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;
import org.zerock.mreview.repository.ReviewRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieImageRepository imageRepository;


    @Transactional
    @Override
    public void modify(MovieDTO movieDTO) {

        Optional<Movie> result = movieRepository.findById(movieDTO.getMno());
        if (result.isPresent()) {
            Movie movie = result.get();
            movie.changeTitle(movieDTO.getTitle());

            movieRepository.save(movie);
        }

        List<MovieImageDTO> movieImages = movieDTO.getImageDTOList();

        List<MovieImageDTO> movieImageDTOList = movieImages.stream().map(movieImage -> {

            return MovieImageDTO.builder()
                    .imgName(movieImage.getImgName())
                    .path(movieImage.getPath())
                    .uuid(movieImage.getUuid())
                    .build();
        }).collect(Collectors.toList());



    }

    @Transactional
    @Override
    public Long register(MovieDTO movieDTO) {

        log.info("register service ");

        Map<String, Object> entityMap = dtoToEntity(movieDTO);
        Movie movie = (Movie) entityMap.get("movie");
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);

        movieImageList.forEach(movieImage -> {
            imageRepository.save(movieImage);
        });

        return movieDTO.getMno();
    }

    @Override
    public MovieDTO getMovie(Long mno) {

        List<Object[]> result = movieRepository.getMovieWithAll(mno);
        Movie movie = (Movie) result.get(0)[0]; // Movie 엔티티는 가장 앞에 존재 - 모든 Row가 동일한 값

        List<MovieImage> movieImageList = new ArrayList<>(); // 영화의 이미지 개수만큼 MovieImage객체 필요

        result.forEach(arr ->{
            MovieImage movieImage = (MovieImage) arr[1];
            movieImageList.add(movieImage);
        });

        Double avg = (Double) result.get(0)[2]; // 평균 평점 - 모든 Row가 동일한 값
        Long reviewCnt = (Long) result.get(0)[3]; // 리뷰 개수 - 모든 Row가 동일한 값

        return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
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
}
