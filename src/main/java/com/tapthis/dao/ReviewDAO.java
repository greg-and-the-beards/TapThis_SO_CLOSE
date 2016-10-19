package com.tapthis.dao;

import java.util.List;

import com.tapthis.entity.ReviewInfo;

public interface ReviewDAO {
	
	List<ReviewInfo> getReviewByBeerName(String beerInfo);
	List<ReviewInfo> getReviewsByUserName(String userName);
	List<ReviewInfo> getOneReviewByUserId(int reviewUserId, int reviewId);
	ReviewInfo getReviewById(int reviewId);
	boolean addReview(ReviewInfo reviewId);
	void updateReview(ReviewInfo reviewId);
	void deleteReview(int reviewId);
	
}
