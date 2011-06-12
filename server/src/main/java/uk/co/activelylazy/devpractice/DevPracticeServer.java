package uk.co.activelylazy.devpractice;

import java.util.Random;

import org.eclipse.jetty.server.Server;

public class DevPracticeServer {

	private Server server;
	int magicNumber = new Random().nextInt();
	private DevPracticeHandler handler;
	
	public DevPracticeServer() {
		server = new Server(8989);
		handler = new DevPracticeHandler(magicNumber);
		server.setHandler(handler);
	}
	
	public void start() throws Exception {
		server.start();
	}
	
	public void stop() throws Exception {
		handler.close();
		server.stop();
	}
}
