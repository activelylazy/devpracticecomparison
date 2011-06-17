package uk.co.activelylazy.devpractice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class Response {

	private String content;
	private String contentType;
	private int status = 200;
	private Map<String, String> headers = new HashMap<String, String>();
	
	public Response(String contentType, String content) {
		this.contentType = contentType;
		this.content = content;
	}

	public String getContent() { return content; }
	public String getContentType() { return contentType; }
	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }
	public Map<String, String> getHeaders() { return Collections.unmodifiableMap(headers); }
	public void addHeader(String header, String value) { headers.put(header, value); }

	public static Response plainText(String content) {
		return new Response("text/plain", content);
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("a HTTP "+status+" response ");
		if (contentType != null) {
			buff.append("of type "+contentType+" ");
		}
		if (!headers.isEmpty()) {
			buff.append("with headers: {");
			for (String header : headers.keySet()) {
				String value = headers.get(header);
				buff.append(header+": "+value+", ");
			}
			buff.append("} ");
		}
		if (content != null) {
			buff.append(" with content "+content);
		}
		return buff.toString();
	}

	public static Response redirect(String location) {
		Response response = new Response(null, null);
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		response.addHeader("Location", location);
		return response;
	}

	public static Response notFound(String content) {
		final Response response = new Response("text/plain", content);
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return response;
	}
}
