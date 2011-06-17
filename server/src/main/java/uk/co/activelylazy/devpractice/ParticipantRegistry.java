package uk.co.activelylazy.devpractice;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticipantRegistry {

	private Map<String, TaskRunner> participants = new HashMap<String, TaskRunner>();
	private List<String> groupNames;
	
	public ParticipantRegistry(String...groupNames) {
		this.groupNames = Arrays.asList(groupNames);
	}
	
	public synchronized void addParticipant(String endpoint, TaskRunner runner) {
		participants.put(endpoint, runner);
	}
	
	public synchronized TaskRunner getParticipantByEndpoint(String endpoint) {
		return participants.get(endpoint);
	}
	
	public synchronized TaskRunner getParticipantByName(String name) {
		for (TaskRunner participant : participants.values()) {
			if (participant.getName().equalsIgnoreCase(name)) {
				return participant;
			}
		}
		return null;
	}
	
	public synchronized void removeParticipant(String endpoint) {
		participants.remove(endpoint);
	}
	
	public synchronized Collection<TaskRunner> getParticipants() {
		return participants.values();
	}
	
	public synchronized void close() {
		for (TaskRunner participant : participants.values()) {
			participant.close();
		}
	}
	
	public boolean isValidGroup(String groupName) {
		return this.groupNames.contains(groupName);
	}

	public List<String> getGroupNames() {
		return this.groupNames;
	}

}
