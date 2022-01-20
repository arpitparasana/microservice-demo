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
import io.movie.moviecatalogueservice.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;

//	@Autowired
//	private WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	@HystrixCommand(fallbackMethod = "getFallBackCatalog")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

		UserRating userRatings = 
				restTemplate.getForObject("http://rating-info/ratings/users/" + userId, 
						UserRating.class);
		
		
		return userRatings.getRatings().stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://movie-info/movies/" + rating.getMovieId(), Movie.class);

			/* Alternative WebClient way
			 * Movie movie = webClientBuilder.build() .get()
			 * .uri("http://localhost:8082/movies/" + rating.getMovieId()) .retrieve()
			 * .bodyToMono(Movie.class) .block();
			 */

			return new CatalogItem(movie.getMovieName(), "Awesome movie", rating.getRating());
		}).collect(Collectors.toList());

	}
	
	public List<CatalogItem> getFallBackCatalog(@PathVariable("userId") String userId) {
		return Arrays.asList(new CatalogItem("No movie", "", 1));
	
	}
}
