package uk.co.activelylazy.devpractice.client;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Client implements Servlet {

	private static final String CLIENT = "http://localhost:9000/";
	private static final String SERVER = "http://localhost:8989/";
	
	private HttpClient client = new DefaultHttpClient();
	
	public HttpClient getClient() { return client; }
	public void setClient(HttpClient client) { this.client = client; }

	@Override
	public void destroy() {
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
	}

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		System.out.println("Got request "+req.getRequestURI());
		
		if (req.getRequestURI().equals("/register")) {
			register();
		}
	}

	public void register() throws ServletException {
		try {
			HttpGet get = new HttpGet(SERVER+"/register?endpoint="+URLEncoder.encode(CLIENT, "UTF-8"));
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			String status = EntityUtils.toString(entity);
			if ( ! status.trim().equals("OK")) {
				throw new ServletException("Got response: "+status);
			}
		} catch (IOException e) {
			throw new ServletException(e);
		}
	}
}
