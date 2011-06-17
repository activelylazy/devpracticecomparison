package uk.co.activelylazy.devpractice.listeners;

import javax.servlet.http.HttpServletRequest;

import uk.co.activelylazy.devpractice.Response;

public class PingListener implements RequestListener {

	@Override
	public Response request(HttpServletRequest request) {
		return Response.plainText("Server OK");
	}

}
