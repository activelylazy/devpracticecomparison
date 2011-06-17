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

import uk.co.activelylazy.devpractice.listeners.RequestListener;

final class DevPracticeHandler extends AbstractHandler {
	protected static final Response NOT_FOUND = Response.notFound("Path not found.\n\nSend /register?endpoint=... to register");
	private Map<String, RequestListener> listeners = new HashMap<String, RequestListener>();

	public DevPracticeHandler(RequestListener redirectListener,
							  RequestListener staticListener,
							  RequestListener pingListener,
							  RequestListener registerListener,
							  RequestListener forceTestListener, RequestListener scoresListener) {
		listeners.put("/", redirectListener);
		listeners.put("/js/jquery.min.js", staticListener);
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
			sendResponse(baseRequest, httpResponse, NOT_FOUND);
			return;
		}			
		Response response = listener.request(request);
		sendResponse(baseRequest, httpResponse, response);
	}

	private void sendResponse(Request baseRequest, HttpServletResponse httpResponse, Response response) throws IOException {
		if (response.getContentType() != null) {
			httpResponse.setContentType(response.getContentType());
		}
		httpResponse.setStatus(response.getStatus());
		if (response.getContent() != null) {
			httpResponse.getWriter().println(response.getContent());
		}
		for (String header : response.getHeaders().keySet()) {
			String value = response.getHeaders().get(header);
			httpResponse.setHeader(header, value);
		}
		baseRequest.setHandled(true);
	}

	public void registerListener(String path, RequestListener listener) {
		listeners.put(path, listener);
	}
}