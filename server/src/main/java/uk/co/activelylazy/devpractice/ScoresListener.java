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
	
	public static class ClientsJSON {
		private ClientJSON[] clients;
		public ClientsJSON(List<TaskRunner> runners) {
			clients = Collections2.transform(runners, new Function<TaskRunner, ClientJSON>() {
				@Override public ClientJSON apply(TaskRunner runner) {
					return new ClientJSON(runner);
				}
			}).toArray(new ClientJSON[]{});
		}
		public ClientJSON[] getClients() { return clients; }
	}

	private final ParticipantRegistry participants;

	public ScoresListener(ParticipantRegistry participants) {
		this.participants = participants;
	}
	
	@Override
	public String request(HttpServletRequest request) throws IOException {
		List<TaskRunner> clients = participants.getParticipants();
		
		ClientsJSON json = new ClientsJSON(clients);
		return JSONSerializer.toJSON(json).toString();
	}

}
