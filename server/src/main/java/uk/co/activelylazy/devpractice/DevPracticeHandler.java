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
	protected static final Response NOT_FOUND = Response.plainText("Path not found.\n\nSend /register?endpoint=... to register");
	private Map<String, RequestListener> listeners = new HashMap<String, RequestListener>();

	public DevPracticeHandler(RequestListener staticListener,
							  RequestListener pingListener,
							  RequestListener registerListener,
							  RequestListener forceTestListener,
							  RequestListener scoresListener) {
		listeners.put("/js/jquery.flot.js", staticListener);
		listeners.put("/index.html", staticListener);
		listeners.put("/ping", pingListener);
		listeners.put("/register", registerListener);
		listeners.put("/forceTest", forceTestListener);
		listeners.put("/scores.json", scoresListener);
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse httpResponse) throws IOException, ServletException {
		RequestListener listener = listeners.get(target);
		if (listener == null) {
			sendResponse(HttpServletResponse.SC_NOT_FOUND, baseRequest, httpResponse, NOT_FOUND);
			return;
		}			
		Response response = listener.request(request);
		sendResponse(HttpServletResponse.SC_OK, baseRequest, httpResponse, response);
	}

	private void sendResponse(int statusCode, Request baseRequest, HttpServletResponse httpResponse, Response response) throws IOException {
		httpResponse.setContentType(response.getContentType());
		httpResponse.setStatus(statusCode);
		baseRequest.setHandled(true);
		httpResponse.getWriter().println(response.getContent());
	}

	public void registerListener(String path, RequestListener listener) {
		listeners.put(path, listener);
	}
}