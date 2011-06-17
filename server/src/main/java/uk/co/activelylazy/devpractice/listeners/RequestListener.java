package uk.co.activelylazy.devpractice.listeners;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import uk.co.activelylazy.devpractice.Response;

public interface RequestListener {

	Response request(HttpServletRequest request) throws IOException;

}
