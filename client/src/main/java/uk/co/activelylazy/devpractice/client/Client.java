package uk.co.activelylazy.devpractice.client;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Client {

	public Client() throws Exception {
		Server server = new Server(9000);
		server.setHandler(new AbstractHandler() {
			
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				System.out.println("Got request for: "+target);
			}
		});
		server.start();
	}
	
	public static void main(String[] args) {
		try {
			Client client = new Client();
			String response = makeRequest("http://localhost:8989/register?endpoint=" + URLEncoder.encode("http://localhost:9000/", "UTF-8"));
			
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static String makeRequest(String request) throws IOException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(request);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity);
	}
}
