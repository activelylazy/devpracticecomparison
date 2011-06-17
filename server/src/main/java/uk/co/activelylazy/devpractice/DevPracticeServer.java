package uk.co.activelylazy.devpractice;

import java.util.Random;

import org.eclipse.jetty.server.Server;

import uk.co.activelylazy.devpractice.listeners.ForceTestListener;
import uk.co.activelylazy.devpractice.listeners.PingListener;
import uk.co.activelylazy.devpractice.listeners.RedirectListener;
import uk.co.activelylazy.devpractice.listeners.RegisterListener;
import uk.co.activelylazy.devpractice.listeners.ScoresListener;
import uk.co.activelylazy.devpractice.listeners.StaticResourceListener;

public class DevPracticeServer {
	private static final String[] GROUP_NAMES = {"TDD", "NoTDD"};
	
	private Server server;
	int magicNumber = new Random().nextInt();
	private DevPracticeHandler handler;
	private ParticipantRegistry participants = new ParticipantRegistry(GROUP_NAMES);
	
	public DevPracticeServer() {
		server = new Server(8989);
		handler = new DevPracticeHandler(new RedirectListener(),
										 new StaticResourceListener(), 
										 new PingListener(),
										 new RegisterListener(participants, new DevPracticeClient.DefaultFactory()), 
										 new ForceTestListener(participants), new ScoresListener(participants));
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
