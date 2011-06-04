package uk.co.activelylazy.devpractice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

public class DevPracticeServerTest {

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
		
		assertThat(content, is("Server OK\n"));
	}

	@Test public void
	after_registering_server_sends_registered_status_message() throws ClientProtocolException, UnsupportedEncodingException, IOException {
		String content = makeRequest("http://localhost:8989/register?endpoint="+URLEncoder.encode("http://localhost:9000/", "UTF-8"));
		
		assertThat(content, is("OK\n"));
		assertThat(client.getStatus(), is("registered"));
	}
	
	@Test public void
	correct_response_to_say_hello_test_generates_pass_status() throws ClientProtocolException, UnsupportedEncodingException, IOException {
		makeRequest("http://localhost:8989/register?endpoint="+URLEncoder.encode("http://localhost:9000/", "UTF-8"));
		client.setNextResponse("Hello world\n\n");
		String content = makeRequest("http://localhost:8989/forceTest?client=0&iteration=0&magic="+server.magicNumber);
		
		assertThat(content, is("OK\n"));
		assertThat(client.getStatus(), is("pass"));
	}
	
	
	private String makeRequest(String url) throws IOException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity);
	}
	
}
