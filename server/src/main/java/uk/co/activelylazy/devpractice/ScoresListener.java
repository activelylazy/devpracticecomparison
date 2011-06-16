package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONSerializer;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class ScoresListener implements RequestListener {
	
	public static class ClientJSON {
		private final String group;
		private final int score;
		private final String endpoint;
		public ClientJSON(TaskRunner runner) {
			this.group = runner.getGroupName();
			this.score = runner.getScore();
			this.endpoint = runner.getEndpoint();
		}
		public String getGroup() { return group; }
		public int getScore() { return score; }
		public String getEndpoint() { return endpoint; }
	}
	
	public static class ClientsJSON {
		private ClientJSON[] clients;
		public ClientsJSON(Collection<TaskRunner> participants) {
			clients = Collections2.transform(participants, new Function<TaskRunner, ClientJSON>() {
				@Override public ClientJSON apply(TaskRunner participant) {
					return new ClientJSON(participant);
				}
			}).toArray(new ClientJSON[]{});
		}
		public ClientJSON[] getClients() { return clients; }
	}

	private final ParticipantRegistry participantRegistry;

	public ScoresListener(ParticipantRegistry participantRegistry) {
		this.participantRegistry = participantRegistry;
	}
	
	@Override
	public Response request(HttpServletRequest request) throws IOException {
		Collection<TaskRunner> participants = participantRegistry.getParticipants();
		
		ClientsJSON json = new ClientsJSON(participants);
		return new Response("text/json", JSONSerializer.toJSON(json).toString());
	}

}
