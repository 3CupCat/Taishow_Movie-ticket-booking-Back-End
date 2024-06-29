package com.taishow.service.client;

import com.taishow.dao.InteractiveRepository;
import com.taishow.dao.MovieRepository;
import com.taishow.dao.ReviewRepository;
import com.taishow.dao.UserRepository;
import com.taishow.dto.CommentsDto;
import com.taishow.dto.ReviewDetailDto;
import com.taishow.entity.Interactive;
import com.taishow.entity.Movie;
import com.taishow.entity.Review;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReviewDetailService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final InteractiveRepository interactiveRepository;
    private final UserRepository userRepository;

    public ReviewDetailService(MovieRepository movieRepository, ReviewRepository reviewRepository,
                               InteractiveRepository interactiveRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
        this.interactiveRepository = interactiveRepository;
        this.userRepository = userRepository;
    }

    public ReviewDetailDto getReviews(Integer userId, Integer movieId){
        ReviewDetailDto reviewDetailDto = new ReviewDetailDto();

        // 取得電影資訊
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("電影不存在"));

        reviewDetailDto.setTitle(movie.getTitle());
        reviewDetailDto.setPoster(movie.getPoster());
        reviewDetailDto.setIsPlaying(movie.isPlaying());

        // 取得分數以及評論數量
        List<Object[]> results = reviewRepository.findScoreDetailByMovieId(movieId);
        if (!results.isEmpty()){
            Object[] result = results.get(0);
            reviewDetailDto.setTotalCommentsNum(Math.toIntExact((Long) result[0]));
            reviewDetailDto.setScoreAvg((Double) result[1]);
            reviewDetailDto.setOneStarRate((Double) result[7]);
            reviewDetailDto.setTwoStarRate((Double) result[8]);
            reviewDetailDto.setThreeStarRate((Double) result[9]);
            reviewDetailDto.setFourStarRate((Double) result[10]);
            reviewDetailDto.setFiveStarRate((Double) result[11]);
        }

        // 取得評論列表
        List<Object[]> results2 = reviewRepository.findCommentDetailByMovieIdAndUserId(movieId, userId);
        List<CommentsDto> commentsList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Object[] result : results2) {
            Integer reviewId = (Integer) result[0];
            String nickName = (String) result[1];
            String photo = (String) result[2];
            String reviewDate = ((LocalDateTime) result[3]).format(formatter);
            Integer score = (Integer) result[4];
            String comment = (String) result[5];
            Integer likeIt = ((Number) result[6]).intValue();
            Integer dislike = ((Number) result[7]).intValue();
            Boolean isLikeIt = (Boolean) result[8];
            Boolean isDislike = (Boolean) result[9];
            Boolean isReport = (Boolean) result[10];

            CommentsDto commentsDto = new CommentsDto(reviewId, nickName, photo, reviewDate, score, comment, likeIt, dislike, isLikeIt, isDislike, isReport);
            commentsList.add(commentsDto);
        }
        reviewDetailDto.setComments(commentsList);
        return reviewDetailDto;
    }

    public void createReview(Integer userId, Review review, Integer movieId){
        LocalDateTime now = LocalDateTime.now();

        review.setUserId(userId);
        review.setMovieId(movieId);
        review.setReviewDate(now);
        reviewRepository.save(review);
    }

    public void updateReview(Integer userId, Review review, Integer movieId){
        Review myReview = reviewRepository.findByUserIdAndMovieId(userId, movieId);
        myReview.setComment(review.getComment());
        myReview.setScore(review.getScore());
        reviewRepository.save(myReview);
    }

    public void deleteReview(Integer userId, Integer movieId){
        reviewRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    public CommentsDto interactionReview(Integer userId, String action, Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("該則評論已被刪除"));

        Interactive interactive;
        try {
            interactive = interactiveRepository.findByReviewIdAndUserId(reviewId, userId);
        } catch (EntityNotFoundException e) {
            // 創建一筆互動紀錄
            interactive = new Interactive();
            interactive.setReviewId(reviewId);
            interactive.setUserId(userId);
            interactive.setLikeit(false);
            interactive.setDislike(false);
            interactive.setReport(false);
        } catch (Exception e) {
            throw new RuntimeException("系統繁忙中，請稍後再試");
        }

        // 依據會員操作，做出對應更新
        switch (action) {
            case "likeit":
                interactive.setLikeit(!interactive.isLikeit());
                interactive.setDislike(false);
                break;
            case "dislike":
                interactive.setLikeit(false);
                interactive.setDislike(!interactive.isDislike());
                break;
            case "report":
                interactive.setReport(true);
                break;
            default:
                throw new IllegalArgumentException("無效的操作");
        }
        interactiveRepository.save(interactive);

        // 取得評論詳細資訊
        List<Object[]> results = reviewRepository.findCommentDetailByReviewIdAndUserId(reviewId, userId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        CommentsDto commentsDto = null;
        for (Object[] result : results) {
            String nickName = (String) result[1];
            String photo = (String) result[2];
            String reviewDate = ((LocalDateTime) result[3]).format(formatter);
            Integer score = (Integer) result[4];
            String comment = (String) result[5];
            Integer likeIt = ((Number) result[6]).intValue();
            Integer dislike = ((Number) result[7]).intValue();
            Boolean isLikeIt = (Boolean) result[8];
            Boolean isDislike = (Boolean) result[9];
            Boolean isReport = (Boolean) result[10];

            commentsDto = new CommentsDto(reviewId, nickName, photo, reviewDate, score, comment, likeIt, dislike, isLikeIt, isDislike, isReport);
        }

        return commentsDto;
    }
}
