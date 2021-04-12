package com.pedrobrandao.heroesapi;

import com.pedrobrandao.heroesapi.document.Heroes;
import com.pedrobrandao.heroesapi.repository.HeroesRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.pedrobrandao.heroesapi.constants.HeroesConstant.HEROES_ENDPOINT_LOCAL;

@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@SpringBootTest
class HeroesapiApplicationTests {

	@Autowired
	WebTestClient webTestClient;

	@Autowired
	HeroesRepository heroesRepository;

	@Test
	public void getOneHeroeById(){
		webTestClient.get().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"), "1")
				.exchange()
				.expectStatus().isOk()
				.expectBody();
	}

	@Test
	public void getOneHeroeNotFound() {
		webTestClient.get().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"), "3")
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	public void saveHeroe(){
		Heroes hero = new Heroes("2", "Super Man", "dc comics", 5);

		webTestClient.post().uri(HEROES_ENDPOINT_LOCAL)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(hero), Heroes.class)
				.exchange()
				.expectStatus().isCreated()
				.expectBody();
	}

	@Test
	public void deleteHeroNotFound(){
		webTestClient.delete().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"), "5")
				.exchange()
				.expectStatus().is5xxServerError()
				.expectBody(Void.class);
	}

	@Test
	public void deleteHero(){
		webTestClient.delete().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"), "2")
				.exchange()
				.expectStatus().isOk()
				.expectBody(Void.class);
	}

}
