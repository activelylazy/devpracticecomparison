package uk.co.activelylazy.devpractice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class DevPracticeServer {

	private Server server;

	public DevPracticeServer() {
		server = new Server(8989);
		server.setHandler(new AbstractHandler() {
			
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				if (target.equals("/ping")) {
					response.setContentType("text/plain");
			        response.setStatus(HttpServletResponse.SC_OK);
			        baseRequest.setHandled(true);
			        response.getWriter().println("Server OK");
				} else if (target.equals("/register")) {
					String endpoint = request.getParameter("endpoint");
					
					DevPracticeClient client = new DevPracticeClient(endpoint);
					client.sendStatus("registered");
					
					response.setContentType("text/plain");
			        response.setStatus(HttpServletResponse.SC_OK);
			        baseRequest.setHandled(true);
			        response.getWriter().println("OK");
				}
			}
		});
	}
	
	public void start() throws Exception {
		server.start();
	}
	
	public void stop() throws Exception {
		server.stop();
	}
}
