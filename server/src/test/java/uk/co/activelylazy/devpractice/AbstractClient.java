package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

abstract public class AbstractClient {
	private Server server;
	
	public AbstractClient() {
		server = new Server(9000);
		server.setHandler(new AbstractHandler() {
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				if (target.startsWith("/Status")) {
					status(request.getParameter("message"));
					baseRequest.setHandled(true);
					return;
				}
				
				StringBuffer content = new StringBuffer();
				char[] buff = new char[1024];
				InputStreamReader reader = new InputStreamReader(request.getInputStream());
				int count;
				while ( (count = reader.read(buff)) > 0 ) {
					content.append(buff, 0, count);
				}
				
				String theResponse = null;
				if (target.startsWith("/SayHelloWorld")) {
					theResponse = sayHelloWorld(request.getParameterMap(), content.toString());
				} else if (target.startsWith("/Echo")) {
					theResponse = echo(request.getParameterMap(), content.toString());
				} else if (target.startsWith("/CountWords")) {
					theResponse = countWords(request.getParameterMap(), content.toString());
				}
				if (theResponse != null) {
					response.getOutputStream().write(theResponse.getBytes());
					response.getOutputStream().flush();
				}
			}
		});
	}
	
	public void start() throws Exception { server.start(); }
	public void stop() throws Exception { server.stop(); }
	
	abstract public void status(String message);
	abstract public String sayHelloWorld(Map<String, String[]> params, String content);
	abstract public String echo(Map<String, String[]> params, String content);
	abstract public String countWords(Map<String, String[]> params, String content);
}
