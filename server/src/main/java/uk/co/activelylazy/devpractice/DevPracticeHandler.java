/**
 * 
 */
package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

final class DevPracticeHandler extends AbstractHandler {
	private List<DevPracticeTask> tasks = new ArrayList<DevPracticeTask>();
	private List<DevPracticeClient> clients = new ArrayList<DevPracticeClient>();
	private int magicNumber;

	String[] texts = new String[] {
			"",
			"word.",
			"A false conclusion: I hate it as an unfilled can. To be up after midnight and to go to bed then, is early: so that to go to bed after midnight is to go to bed betimes. Does not our life consist of the four elements?",
			"I did impeticos thy gratillity; for Malvolio\'s nose is no whipstock: my lady has a white hand, and the Myrmidons are no bottle-ale houses.",
			"What a caterwauling do you keep here! If my lady have not called up her steward Malvolio and bid him turn you out of doors, never trust me.",
			"My masters, are you mad? or what are you? Have ye no wit, manners, nor honesty, but to gabble like tinkers at this time of night? Do ye make an alehouse of my lady's house, that ye squeak out your coziers' catches without any mitigation or remorse of voice? Is there no respect of place, persons, nor time in you?",
			"'Twere as good a deed as to drink when a man's a-hungry, to challenge him the field, and then to break promise with him and make a fool of him.",
			"I will drop in his way some obscure epistles of love; wherein, by the colour of his beard, the shape of his leg, the manner of his gait, the expressure of his eye, forehead, and complexion, he shall find himself most feelingly personated. I can write very like my lady your niece: on a forgotten matter we can hardly make distinction of our hands.",
			"Come, come, I'll go burn some sack; 'tis too late to go to bed now: come, knight; come, knight.",
			"Not so, sir, I do care for something; but in my conscience, sir, I do not care for you: if that be to care for nothing, sir, I would it would make you invisible.",
			"The matter, I hope, is not great, sir, begging but a beggar: Cressida was a beggar. My lady is within, sir. I will construe to them whence you come; who you are and what you would are out of my welkin, I might say 'element,' but the word is over-worn.",
			"I am invariably late for appointments - sometimes as much as two hours. I've tried to change my ways but the things that make me late are too strong, and too pleasing."
		};

	public DevPracticeHandler(int magicNumber) {
		this.magicNumber = magicNumber;
		tasks.add(new SayHelloWorldTask());
		tasks.add(new EchoContentBackTask());
		tasks.add(new CountWordsTask());
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (target.equals("/ping")) {
			sendResponse(baseRequest, response, "Server OK");
		} else if (target.equals("/register")) {
			String endpoint = request.getParameter("endpoint");
			
			DevPracticeClient client = new DevPracticeClient(endpoint);
			clients.add(client);
			client.sendStatus("registered");
			client.start();

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
	
	public void close() {
		for (DevPracticeClient client : clients) {
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