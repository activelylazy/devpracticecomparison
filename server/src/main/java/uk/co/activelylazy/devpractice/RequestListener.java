package uk.co.activelylazy.devpractice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface RequestListener {

	String request(HttpServletRequest request) throws IOException;

}
