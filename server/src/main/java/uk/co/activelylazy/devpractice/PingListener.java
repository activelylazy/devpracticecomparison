package uk.co.activelylazy.devpractice;

import javax.servlet.http.HttpServletRequest;

public class PingListener implements RequestListener {

	@Override
	public Response request(HttpServletRequest request) {
		return Response.plainText("Server OK");
	}

}
