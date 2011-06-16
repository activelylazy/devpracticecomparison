package uk.co.activelylazy.devpractice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.co.activelylazy.devpractice.ClientMatcher.that_the_server;
import static uk.co.activelylazy.devpractice.WhenTheClient.when_the_client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DevPracticeServerIntegrationTest {

	private DevPracticeServer server;
	private StubClient client;

	@Before public void start_client_and_server() throws Exception {
		server = new DevPracticeServer();
		server.start();
		client = new StubClient();
		client.start();
	}
	
	@After public void stop_client_and_server() throws Exception {
		server.stop();
		client.stop();
	}
	
	@Test public void
	server_responds_to_ping() throws IOException {
		String content = makeRequest("http://localhost:8989/ping");
		
		assertThat(content.trim(), is("Server OK"));
	}
	
	@Test public void
	after_registering_server_sends_registered_status_message() throws ClientProtocolException, UnsupportedEncodingException, IOException {
		String content = after_registering_the_client();
		
		assertThat(content.trim(), is("OK"));
		assertThat(client.getStatus(), is("registered"));
	}

	@Test public void
	response_to_say_hello_test_generates_correct_status() throws ClientProtocolException, UnsupportedEncodingException, IOException {
		after_registering_the_client();
		
		assertThat(when_the_client(client)
						.forces_the_server(server)
						.to_run_task(0)
						.and_responds_with("Hello world\n\n")
						.the_result(),
					is(that_the_server().sent_status("pass").after_requesting_url("/SayHelloWorld")));

		assertThat(when_the_client(client)
						.forces_the_server(server)
						.to_run_task(0)
						.and_responds_with("Goodbye, cruel world")
						.the_result(),
					is(that_the_server().sent_status("fail").after_requesting_url("/SayHelloWorld")));
	}
	
	@Test public void
	echo_contents_back_test_gives_correct_status() throws ClientProtocolException, UnsupportedEncodingException, IOException {
		after_registering_the_client();
		
		assertThat(when_the_client(client)
						.forces_the_server(server)
						.to_run_task(1)
						.and_responds_with("Echo this text back\n")
						.the_result(),
					is(that_the_server().sent_status("pass").after_requesting_url("/Echo")));

		assertThat(when_the_client(client)
				.forces_the_server(server)
				.to_run_task(1)
				.and_responds_with("Something else\n")
				.the_result(),
				is(that_the_server().sent_status("fail").after_requesting_url("/Echo")));
	}
	
	@Test public void
	count_words_test_gives_correct_status() throws ClientProtocolException, UnsupportedEncodingException, IOException {
		after_registering_the_client();
		
		assertThat(when_the_client(client)
				.forces_the_server(server)
				.to_run_task(2)
				.with_text(0)
				.and_responds_with("0\n")
				.the_result(),
			is(that_the_server().sent_status("pass").after_requesting_url("/CountWords")));
		
		assertThat(when_the_client(client)
				.forces_the_server(server)
				.to_run_task(2)
				.with_text(1)
				.and_responds_with("1\n")
				.the_result(),
			is(that_the_server().sent_status("pass").after_requesting_url("/CountWords")));
		
		assertThat(when_the_client(client)
				.forces_the_server(server)
				.to_run_task(2)
				.with_text(2)
				.and_responds_with("46\n")
				.the_result(),
				is(that_the_server().sent_status("pass").after_requesting_url("/CountWords")));
		
		assertThat(when_the_client(client)
				.forces_the_server(server)
				.to_run_task(2)
				.with_text(2)
				.and_responds_with("10\n")
				.the_result(),
				is(that_the_server().sent_status("fail - expected 46; you sent 10").after_requesting_url("/CountWords")));
	}
	
	@Test public void
	score_after_registering_is_zero() throws ClientProtocolException, IOException {
		String endpoint = "http://localhost:9000/";
		String encodedEndpoint = URLEncoder.encode(endpoint, "UTF-8");
		after_registering_the_client();
		
		String response = makeRequest("http://localhost:8989/scores.json?client="+encodedEndpoint).trim();
		assertThat(response, is("{\"clients\":[{\"endpoint\":\""+endpoint+"\",\"group\":\"TDD\",\"score\":0}]}"));
	}
	
	private String makeRequest(String url) throws IOException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity);
	}
	
	private String after_registering_the_client() throws IOException, ClientProtocolException, UnsupportedEncodingException {
		String content = makeRequest("http://localhost:8989/register"+
				"?endpoint=" + URLEncoder.encode(client.getEndpoint(), "UTF-8") +
				"&group=TDD" +
				"&runTests=false");
		return content;
	}

}