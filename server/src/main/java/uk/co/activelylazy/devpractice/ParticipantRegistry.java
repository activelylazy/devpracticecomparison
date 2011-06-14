package uk.co.activelylazy.devpractice;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class ParticipantRegistry {

	private Map<String, ParticipantGroup> groups = new HashMap<String, ParticipantGroup>();
	
	public ParticipantRegistry(String...groupNames) {
		for (String groupName : groupNames) {
			groups.put(groupName, new ParticipantGroup(groupName));
		}
	}
	
	public void addParticipant(String endpoint, TaskRunner runner, String groupName) {
		getGroup(groupName).addParticipant(endpoint, runner);
	}
	
	public TaskRunner getParticipant(String endpoint, String groupName) {
		return getGroup(groupName).getParticipant(endpoint);
	}
	
	public List<ParticipantGroup> getParticipantGroups() {
		return Lists.newArrayList(groups.values());
	}

	public void close() {
		for (ParticipantGroup group : groups.values()) {
			group.close();
		}
	}
	
	private ParticipantGroup getGroup(String groupName) {
		return groups.get(groupName);
	}

	public boolean isValidGroup(String groupName) {
		return groups.containsKey(groupName);
	}

	public Collection<String> getGroupNames() {
		return groups.keySet();
	}
}
