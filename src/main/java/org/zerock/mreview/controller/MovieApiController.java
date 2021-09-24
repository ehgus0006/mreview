package org.zerock.mreview.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.service.MovieService;

@RestController
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieApiController {


    private final MovieService movieService;

    // 수정
    @PutMapping("/modify/{mno}")
    public String modify(@PathVariable Long mno, MovieDTO movieDTO) {

        log.info("수정 :" + movieDTO);

        return "redirect:/movie/list";
    }
}
