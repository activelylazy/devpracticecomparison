package uk.co.activelylazy.devpractice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class RedirectListener implements RequestListener {

	@Override
	public Response request(HttpServletRequest request) throws IOException {
		return Response.redirect("/index.html");
	}

}
