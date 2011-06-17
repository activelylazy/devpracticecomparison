package uk.co.activelylazy.devpractice;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.co.activelylazy.devpractice.tasks.CountWordsTask;

public class DevPracticeServerSequenceIntegrationTest {

	private DevPracticeServer server;

	@Before public void start_client_and_server() throws Exception {
		server = new DevPracticeServer();
		server.start();
	}
	
	@After public void stop_client_and_server() throws Exception {
		server.stop();
	}

	@Test public void
	client_sees_tasks_in_sequence() throws Exception {
		final List<String> callSequence = new ArrayList<String>();
		AbstractClient client = new AbstractClient() {
			@Override
			public void status(String message) {
				callSequence.add("Status: "+message);
			}
			@Override
			public String sayHelloWorld(Map<String, String[]> params, String content) {
				callSequence.add("Say hello world");
				return "Hello world\n";
			}
			@Override
			public String echo(Map<String, String[]> params, String content) {
				callSequence.add("Echo");
				return content;
			}
			@Override
			public String countWords(Map<String, String[]> params, String content) {
				callSequence.add("Count words");
				return Integer.toString(new CountWordsTask().countWords(content));
			}
		};
		client.start();
		
		try {
			makeRequest("http://localhost:8989/register"+
					"?endpoint="+URLEncoder.encode("http://localhost:9000/", "UTF-8")+
					"&group=TDD&name=Sequence%20Client");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// Ignore
			}
		} finally {
			client.stop();
		}
	
		assertThat(callSequence, Matchers.contains("Status: registered","Say hello world", "Status: pass", "Echo", "Status: pass", 
				"Count words", "Status: pass"));
	}
	
	private String makeRequest(String url) throws IOException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity);
	}
	
}
