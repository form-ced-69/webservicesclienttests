package webservicesclienttests;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.WireMockServer;

import fr.orsys.webservicesclienttests.Quote;

public class TestWireMock {

	private static RestTemplate restTemplate;
	private WireMockServer wireMockServer = new WireMockServer();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		restTemplate = new RestTemplate();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		wireMockServer.start();
		
		configureFor("localhost", 8080);
		
		InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("wiremock-responses.json");
		String jsonString = convertInputStreamToString(jsonInputStream);
		
		stubFor(get(urlEqualTo("/test-wire-mock")).willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withHeader("charset", "UTF-8")
				.withBody(jsonString)));
	}

	@After
	public void tearDown() throws Exception {
		
		wireMockServer.stop();
	}

	@Test
	public void test() {
		Quote quote = restTemplate.getForObject("http://localhost:8080/test-wire-mock", Quote.class);
		
		verify(getRequestedFor(urlEqualTo("/test-wire-mock")));
		
        assertNotNull(quote);
        assertNotNull(quote.getType());
        assertEquals("success", quote.getType());
        assertNotNull(quote.getValue());
        assertNotNull(quote.getValue().getId());
        assertEquals(9, quote.getValue().getId().longValue());
        assertNotNull(quote.getValue().getQuote());
        assertEquals("So easy it is to switch container in #springboot.", quote.getValue().getQuote());
	}

	private String convertInputStreamToString(InputStream inputStream) {
	    Scanner scanner = new Scanner(inputStream, "UTF-8");
	    String string = scanner.useDelimiter("\\Z").next();
	    scanner.close();
	    return string;
	}
}
