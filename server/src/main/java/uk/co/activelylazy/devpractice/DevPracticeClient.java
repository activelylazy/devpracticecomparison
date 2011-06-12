package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class DevPracticeClient {

	private String endpoint;
	
	public DevPracticeClient(String endpoint) {
		this.endpoint = endpoint;
		if (this.endpoint.endsWith("/")) {
			this.endpoint += "/";
		}
	}
	
	public void sendStatus(String status) throws ClientProtocolException, IOException {
		makeRequest("Status?message="+URLEncoder.encode(status, "UTF-8"));
	}
	
	public String makeRequest(String requestURI) throws IOException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(endpoint + requestURI);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity);
	}
	
	public String makePOSTRequest(String requestURI, String body) throws IOException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(endpoint + requestURI);
		HttpEntity requestEntity = new StringEntity(body, "text/plain", "UTF-8");
		post.setEntity(requestEntity);
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity);
	}
	
}
