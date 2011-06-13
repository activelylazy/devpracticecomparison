package uk.co.activelylazy.devpractice;

import java.util.HashMap;
import java.util.Map;

public class ParticipantRegistry {

	private Map<String, TaskRunner> clients = new HashMap<String, TaskRunner>();
	
	public void addParticipant(String endpoint, TaskRunner runner) {
		clients.put(endpoint, runner);
	}
	
	public TaskRunner getParticipant(String endpoint) {
		return clients.get(endpoint);
	}
	
	public void close() {
		for (TaskRunner client : clients.values()) {
			client.close();
		}
	}
	
}
