package uk.co.activelylazy.devpractice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONSerializer;

public class ScoresListener implements RequestListener {
	
	public static class Score {
		private int score;
		public Score(int score) { this.score = score; }
		public int getScore() { return score; }
	}

	private final ParticipantRegistry participants;

	public ScoresListener(ParticipantRegistry participants) {
		this.participants = participants;
	}
	
	@Override
	public String request(HttpServletRequest request) throws IOException {
		String endpoint = request.getParameter("client");
		TaskRunner participant = participants.getParticipant(endpoint);
		
		Score score = new Score(participant.getScore());
		return JSONSerializer.toJSON(score).toString();
	}

}
