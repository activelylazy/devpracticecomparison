/**
 * 
 */
package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

final class DevPracticeHandler extends AbstractHandler {
	private Map<String, TaskRunner> clients = new HashMap<String, TaskRunner>();
	private int magicNumber;

	public DevPracticeHandler(int magicNumber) {
		this.magicNumber = magicNumber;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (target.equals("/ping")) {
			sendResponse(baseRequest, response, "Server OK");
		} else if (target.equals("/register")) {
			try {
				String endpoint = request.getParameter("endpoint");
				
				DevPracticeClient client = new DevPracticeClient(endpoint);
				TaskRunner runner = new TaskRunner(client);
				clients.put(endpoint, runner);
				client.sendStatus("registered");
				if (request.getParameter("runTests") == null) { 
					runner.start();
				}
	
				sendResponse(baseRequest, response, "OK");
			} catch (Throwable t) {
				t.printStackTrace();
				sendResponse(baseRequest, response, "Error: "+t.getMessage());
			}
		} else if (target.equals("/forceTest") && Integer.parseInt(request.getParameter("magic")) == magicNumber) {
			String client = request.getParameter("client");
			int iteration = Integer.parseInt(request.getParameter("iteration"));
			int text = 0;
			if (request.getParameter("text") != null) {
				text = Integer.parseInt(request.getParameter("text"));
			}
			
			TaskRunner theClient = clients.get(client);
			if (theClient == null) {
				throw new NullPointerException("No such client "+client);
			}
			theClient.executeTask(iteration, text);
			
			sendResponse(baseRequest, response, "OK");
		}
	}

	public void close() {
		for (TaskRunner client : clients.values()) {
			client.close();
		}
	}

	private void sendResponse(Request baseRequest, HttpServletResponse response, String message) throws IOException {
		response.setContentType("text/plain");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println(message);
	}
}