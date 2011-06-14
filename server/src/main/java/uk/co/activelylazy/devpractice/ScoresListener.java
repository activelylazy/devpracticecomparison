package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONSerializer;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class ScoresListener implements RequestListener {
	
	public static class ClientJSON {
		private final int score;
		private final String endpoint;
		public ClientJSON(TaskRunner runner) {
			this.score = runner.getScore();
			this.endpoint = runner.getEndpoint();
		}
		public int getScore() { return score; }
		public String getEndpoint() { return endpoint; }
	}
	
	public static class GroupJSON {
		private String name;
		private ClientJSON[] clients;
		public GroupJSON(ParticipantGroup group) {
			name = group.getGroupName();
			clients = Collections2.transform(group.getParticipants(), new Function<TaskRunner, ClientJSON>() {
				@Override public ClientJSON apply(TaskRunner runner) {
					return new ClientJSON(runner);
				}
			}).toArray(new ClientJSON[]{});
		}
		public String getName() { return name; }
		public ClientJSON[] getClients() { return clients; }
	}
	
	public static class GroupsJSON {
		private GroupJSON[] groups;
		public GroupsJSON(List<ParticipantGroup> theGroups) {
			groups = Collections2.transform(theGroups, new Function<ParticipantGroup, GroupJSON>() {
				@Override public GroupJSON apply(ParticipantGroup group) {
					return new GroupJSON(group);
				}
			}).toArray(new GroupJSON[]{});
		}
		public GroupJSON[] getGroups() { return groups; }
	}

	private final ParticipantRegistry participants;

	public ScoresListener(ParticipantRegistry participants) {
		this.participants = participants;
	}
	
	@Override
	public String request(HttpServletRequest request) throws IOException {
		List<ParticipantGroup> groups = participants.getParticipantGroups();
		
		GroupsJSON json = new GroupsJSON(groups);
		return JSONSerializer.toJSON(json).toString();
	}

}
