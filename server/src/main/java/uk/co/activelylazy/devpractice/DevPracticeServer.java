package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class DevPracticeServer {

	private Server server;
	private List<DevPracticeTask> tasks = new ArrayList<DevPracticeTask>();
	int magicNumber = new Random().nextInt();
	
	private String[] texts = new String[] {
		"",
		"word.",
		"A false conclusion: I hate it as an unfilled can. To be up after midnight and to go to bed then, is early: so that to go to bed after midnight is to go to bed betimes. Does not our life consist of the four elements?"
	};

	public DevPracticeServer() {
		tasks.add(new SayHelloWorldTask());
		tasks.add(new EchoContentBackTask());
		tasks.add(new CountWordsTask());
		
		server = new Server(8989);
		server.setHandler(new AbstractHandler() {
			
			private List<DevPracticeClient> clients = new ArrayList<DevPracticeClient>();
			
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				if (target.equals("/ping")) {
					sendResponse(baseRequest, response, "Server OK");
				} else if (target.equals("/register")) {
					String endpoint = request.getParameter("endpoint");
					
					DevPracticeClient client = new DevPracticeClient(endpoint);
					clients.add(client);
					client.sendStatus("registered");

					sendResponse(baseRequest, response, "OK");
				} else if (target.equals("/forceTest") && Integer.parseInt(request.getParameter("magic")) == magicNumber) {
					int client = Integer.parseInt(request.getParameter("client"));
					int iteration = Integer.parseInt(request.getParameter("iteration"));
					int text = 0;
					if (request.getParameter("text") != null) {
						text = Integer.parseInt(request.getParameter("text"));
					}
					
					DevPracticeClient theClient = clients.get(client);
					tasks.get(iteration).executeFor(theClient,texts[text]);
					
					sendResponse(baseRequest, response, "OK");
				}
			}

			private void sendResponse(Request baseRequest, HttpServletResponse response, String message) throws IOException {
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_OK);
				baseRequest.setHandled(true);
				response.getWriter().println(message);
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
