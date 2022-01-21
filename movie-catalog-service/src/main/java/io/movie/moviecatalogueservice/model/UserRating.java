package io.movie.moviecatalogueservice.model;

import java.util.List;

public class UserRating {

	private String userId;
	private List<Rating> ratings;

	public UserRating() {
	}
	
	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}

}
