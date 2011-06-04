package uk.co.activelylazy.devpractice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DevPracticeServerTest {

	private DevPracticeServer server;

	@Before public void start_server() throws Exception {
		server = new DevPracticeServer();
		server.start();
	}
	
	@After public void stop_server() throws Exception {
		server.stop();
	}
	
	@Test public void
	server_responds_to_ping() throws IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://localhost:8989/ping");
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		String content = EntityUtils.toString(entity);
		
		assertThat(content, is("Server OK\n"));
	}
}
