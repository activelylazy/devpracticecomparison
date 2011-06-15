package uk.co.activelylazy.devpractice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public interface RequestListener {

	Response request(HttpServletRequest request) throws IOException;

}
