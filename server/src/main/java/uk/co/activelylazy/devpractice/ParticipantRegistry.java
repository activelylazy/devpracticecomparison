package uk.co.activelylazy.devpractice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class ParticipantRegistry {

	private Map<String, TaskRunner> clients = new HashMap<String, TaskRunner>();
	
	public void addParticipant(String endpoint, TaskRunner runner) {
		clients.put(endpoint, runner);
	}
	
	public TaskRunner getParticipant(String endpoint) {
		return clients.get(endpoint);
	}
	
	public List<TaskRunner> getParticipants() {
		return Lists.newArrayList(clients.values());
	}

	public void close() {
		for (TaskRunner client : clients.values()) {
			client.close();
		}
	}
}
