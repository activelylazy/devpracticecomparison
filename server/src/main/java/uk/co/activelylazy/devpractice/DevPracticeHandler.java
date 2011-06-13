/**
 * 
 */
package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

final class DevPracticeHandler extends AbstractHandler {
	protected static final String NOT_FOUND_MESSAGE = "Path not found.\n\nSend /register?endpoint=... to register";
	private Map<String, RequestListener> listeners = new HashMap<String, RequestListener>();

	public DevPracticeHandler(RequestListener pingListener,
							  RequestListener registerListener,
							  RequestListener forceTestListener) {
		listeners.put("/ping", pingListener);
		listeners.put("/register", registerListener);
		listeners.put("/forceTest", forceTestListener);
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		RequestListener listener = listeners.get(target);
		if (listener == null) {
			sendResponse(HttpServletResponse.SC_NOT_FOUND, baseRequest, response, NOT_FOUND_MESSAGE);
			return;
		}			
		String responseText = listener.request(request);
		sendResponse(HttpServletResponse.SC_OK, baseRequest, response, responseText);
	}

	private void sendResponse(int statusCode, Request baseRequest, HttpServletResponse response, String message) throws IOException {
		response.setContentType("text/plain");
		response.setStatus(statusCode);
		baseRequest.setHandled(true);
		response.getWriter().println(message);
	}

	public void registerListener(String path, RequestListener listener) {
		listeners.put(path, listener);
	}
}