package com.udacity.pricing;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingServiceApplicationTests {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@Test
	public void contextLoads() {
	}

	@Test
	public void canRetrievePrices() throws Exception {
		var entity = new HttpEntity<String>(null, headers);
		var response = restTemplate.exchange(
				createUrl("/prices/1"), HttpMethod.GET, entity, String.class);

		String expected = "{\"currency\":\"USD\",\"vehicleId\":1}";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void canSearchByVehicleId() throws Exception {
		var entity = new HttpEntity<String>(null, headers);
		var response = restTemplate.exchange(
				createUrl("/prices/search/findByVehicleId?vehicleId=2"), HttpMethod.GET, entity, String.class);

		String expected = "{\"currency\":\"USD\",\"vehicleId\":2}";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	private String createUrl(String path) {
		return "http://localhost:" + port + path;
	}

}
