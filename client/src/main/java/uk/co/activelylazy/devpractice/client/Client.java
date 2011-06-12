package uk.co.activelylazy.devpractice.client;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import uk.co.activelylazy.devpractice.client.RequestHandler.Factory;

public class Client implements Servlet {

	private Factory factory;
	
	@Override
	public void destroy() {
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
	}

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		String requestURI = req.getRequestURI();
		Map<String, String[]> params = req.getParameterMap();
		
		String responseBody = factory.create().handle(requestURI, params);
		
		response.getOutputStream().write(responseBody.getBytes());
		response.getOutputStream().flush();
	}

	public void setRequestHandlerFactory(Factory factory) { this.factory = factory; }
}
