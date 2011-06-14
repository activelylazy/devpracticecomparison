package uk.co.activelylazy.devpractice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class StubClient {

	private Server server;
	private String lastRequestedURL;
	private String nextResponse;
	private String status;
	
	public StubClient() {
		server = new Server(9000);
		server.setHandler(new AbstractHandler() {
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				if (target.startsWith("/Status")) {
					setStatus(request.getParameter("message"));
					baseRequest.setHandled(true);
					return;
				}
				
				setLastRequestedURL(target);
				String theResponse = getNextResponse();
				if (theResponse == null) {
					throw new NullPointerException("No response to send");
				}
				response.getOutputStream().write(theResponse.getBytes());
				response.getOutputStream().flush();
				setNextResponse(null);
			}
		});
	}
	
	public void start() throws Exception {
		server.start();
	}
	
	public void stop() throws Exception {
		server.stop();
	}
	
	public synchronized void setNextResponse(String nextResponse) { this.nextResponse = nextResponse; }
	private synchronized final String getNextResponse() { return this.nextResponse; }
	public synchronized String getLastRequestedURL() { return lastRequestedURL; }
	private synchronized void setLastRequestedURL(String lastRequestedURL) { this.lastRequestedURL = lastRequestedURL; }
	public synchronized String getStatus() { return status; }
	private synchronized void setStatus(String status) { this.status = status; }
	
	public String getEndpoint() {
		return "http://localhost:9000/";
	}
}
