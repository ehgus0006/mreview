package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMoviewReviews() {

        // 200 개의 리뷰를 등록
        IntStream.rangeClosed(1,200).forEach(i -> {

            // 영화번호
            Long mno = (long)(Math.random() * 100) + 1;
            System.out.println("영화번호는" + mno);
            // 리뷰어 번호
            Long mid = ((long) (Math.random() * 100) + 1);
            System.out.println("리뷰어 번호는" + mid);

            Member member = Member.builder().mid(mid).build();

            Review review = Review.builder()
                    .movie(Movie.builder().mno(mno).build())
                    .member(member)
                    .grade((int) (Math.random() * 5) + 1)
                    .text("이 영화에 대한 느낌.." + i)
                    .build();
            reviewRepository.save(review);
        });
    }

    @Transactional
    @Commit
    @Test
    public void testDeleteMember() {
        Long mid = 1L;
        Member member = Member.builder().mid(mid).build();

        memberRepository.deleteById(mid);
        reviewRepository.deleteByMember(member);

    }

    @Test
    public void testGetMoviewReviews() {
        Movie movie = Movie.builder().mno(93L).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        result.forEach(movieReview -> {
            System.out.print(movieReview.getReviewnum());
            System.out.print("\t" + movieReview.getGrade());
            System.out.print("\t" + movieReview.getText());
            System.out.print("\t" + movieReview.getMember().getEmail());
            System.out.println("--------------------------------------");
        });
    }



}