package uk.co.activelylazy.devpractice;

import java.util.Random;

import org.eclipse.jetty.server.Server;

public class DevPracticeServer {

	private Server server;
	int magicNumber = new Random().nextInt();
	private DevPracticeHandler handler;
	private ParticipantRegistry participants = new ParticipantRegistry();
	
	public DevPracticeServer() {
		server = new Server(8989);
		handler = new DevPracticeHandler(new PingListener(), 
										 new RegisterListener(participants, new DevPracticeClient.DefaultFactory()),
										 new ForceTestListener(participants));
		server.setHandler(handler);
	}
	
	public void start() throws Exception {
		server.start();
	}
	
	public void stop() throws Exception {
		participants.close();
		server.stop();
	}
	
	public static void main(String[] args) {
		DevPracticeServer server = new DevPracticeServer();
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// Ignore
			}
		}
	}
}
