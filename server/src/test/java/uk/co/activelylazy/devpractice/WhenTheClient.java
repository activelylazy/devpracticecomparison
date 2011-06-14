package uk.co.activelylazy.devpractice;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

class WhenTheClient {
	
	private final StubClient client;
	private String responseText;
	private int iteration;
	private DevPracticeServer server;
	private Integer text;
	
	public WhenTheClient(StubClient client) { this.client = client; }
	
	public static WhenTheClient when_the_client(StubClient client) {
		return new WhenTheClient(client);
	}

	public WhenTheClient forces_the_server(DevPracticeServer server) {
		this.server = server;
		return this;
	}
	
	public WhenTheClient and_responds_with(final String responseText) {
		this.responseText = responseText;
		return this;
	}
	
	public WhenTheClient to_run_task(int iteration) {
		this.iteration = iteration;
		return this;
	}
	
	public StubClient the_result() throws ClientProtocolException, IOException {
		client.setNextResponse(responseText);
		forceTest(client.getEndpoint(), iteration);
		return client;
	}

	public WhenTheClient with_text(int text) {
		this.text = text;
		return this;
	}
	
	private void forceTest(String endpoint, int iteration) throws IOException, ClientProtocolException {
		makeRequest("http://localhost:8989/forceTest"+
				"?client=" + endpoint + 
				"&group=TDD" +
				(text != null ? "&text="+text : "") +
				"&iteration=" + iteration + 
				"&magic=" + server.magicNumber);
	}

	private String makeRequest(String url) throws IOException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity);
	}
}