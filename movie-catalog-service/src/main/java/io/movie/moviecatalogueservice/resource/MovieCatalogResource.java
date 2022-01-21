package io.movie.moviecatalogueservice.resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.movie.moviecatalogueservice.model.CatalogItem;
import io.movie.moviecatalogueservice.model.Movie;
import io.movie.moviecatalogueservice.model.Rating;
import io.movie.moviecatalogueservice.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;

//	@Autowired
//	private WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

		UserRating userRatings = getRatings(userId);
		
		return userRatings.getRatings().stream()
				.map(rating -> getCatalogItem(rating)).collect(Collectors.toList());

	}

	@HystrixCommand(fallbackMethod = "getFallBackCatalogItem")
	private CatalogItem getCatalogItem(Rating rating) {
		Movie movie = restTemplate.getForObject("http://movie-info/movies/" + rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getMovieName(), "Awesome movie", rating.getRating());
	}
	

	public CatalogItem getFallBackCatalogItem(Rating rating) {
		return new CatalogItem("Movie name not found", "", rating.getRating());
	}
	

	@HystrixCommand(fallbackMethod = "getFallBackUserRating")
	private UserRating getRatings(String userId) {
		return restTemplate.getForObject("http://rating-info/ratings/users/" + userId, 
				UserRating.class);
	}
	
	@SuppressWarnings("unused")
	private UserRating getFallBackUserRating(String userId) {
		UserRating rating = new UserRating();
		rating.setRatings(Arrays.asList(new Rating("0",0)));
		rating.setUserId(userId);
		
		return rating;
	
	}
}
