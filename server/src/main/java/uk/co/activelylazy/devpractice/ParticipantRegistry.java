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
	
	public void addParticipant(String endpoint, TaskRunner runner) {
		if (!isValidGroup(runner.getGroupName())) {
			throw new IllegalArgumentException("Invalid group name: "+runner.getGroupName());
		}
		participants.put(endpoint, runner);
	}
	
	public TaskRunner getParticipant(String endpoint) {
		return participants.get(endpoint);
	}
	
	public Collection<TaskRunner> getParticipants() {
		return participants.values();
	}
	
	public void close() {
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
