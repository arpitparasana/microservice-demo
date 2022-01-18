package io.movie.ratingsinfoservice.resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.movie.ratingsinfoservice.model.Rating;

@RestController
@RequestMapping("/ratings")
public class RatingsInfoResource {

	@RequestMapping("/{movieId}")
	public Rating getRating(@PathVariable("movieId") String movieId) {
		
		return new Rating(movieId, 10);
		
	}
}
