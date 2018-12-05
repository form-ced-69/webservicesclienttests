package webservicesclienttests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import fr.orsys.webservicesclienttests.Quote;

public class TestRestTemplate {

	private static RestTemplate restTemplate;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		restTemplate = new RestTemplate();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        assertNotNull(quote);
        assertNotNull(quote.getType());
        assertEquals("success", quote.getType());
        assertNotNull(quote.getValue());
        assertNotNull(quote.getValue().getId());
        assertNotNull(quote.getValue().getQuote());
	}

}
