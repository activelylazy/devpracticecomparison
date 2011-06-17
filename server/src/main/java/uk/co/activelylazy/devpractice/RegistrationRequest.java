package uk.co.activelylazy.devpractice;

import javax.servlet.http.HttpServletRequest;

public class RegistrationRequest {

	private String endpoint;
	private String groupName;
	private boolean runTests;
	private String clientName;
	
	public RegistrationRequest(HttpServletRequest request) {
		this.endpoint = request.getParameter("endpoint");
		this.groupName = request.getParameter("group");
		this.clientName = request.getParameter("name");
		this.runTests = request.getParameter("runTests") == null; 
	}

	public String getEndpoint() { return endpoint; }
	public String getGroupName() { return groupName; }
	public boolean isRunTests() { return runTests; }
	public String getClientName() { return clientName; }
}
