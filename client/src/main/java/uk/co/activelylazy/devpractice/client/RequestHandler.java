package uk.co.activelylazy.devpractice.client;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RequestHandler {

	private static final String CLIENT = "http://localhost:9000/";
	private static final String SERVER = "http://localhost:8989/";

	private HttpClient client = new DefaultHttpClient();

	public interface Factory { public RequestHandler create(); }
	public static class DefaultFactory implements Factory {
		@Override public RequestHandler create() { return new RequestHandler(); }
	}

	public HttpClient getClient() { return client; }
	public void setClient(HttpClient client) { this.client = client; }

	public String handle(String uri, Map<String, String[]> params) throws ServletException {
		if (uri.equals("register")) {
			return register();
		} else if (uri.equals("SayHelloWorld")) {
			return sayHelloWorld();
		}
		return null;
	}
	
	private String register() throws ServletException {
		try {
			HttpGet get = new HttpGet(SERVER+"/register?endpoint="+URLEncoder.encode(CLIENT, "UTF-8"));
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			String status = EntityUtils.toString(entity);
			if ( ! status.trim().equals("OK")) {
				throw new ServletException("Got response: "+status);
			}
			return "OK";
		} catch (IOException e) {
			throw new ServletException(e);
		}
	}
	
	private String sayHelloWorld() {
		return "Hello world";
	}
}
