package uk.co.activelylazy.devpractice.listeners;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import uk.co.activelylazy.devpractice.Response;

public class RedirectListener implements RequestListener {

	@Override
	public Response request(HttpServletRequest request) throws IOException {
		return Response.redirect("/index.html");
	}

}
