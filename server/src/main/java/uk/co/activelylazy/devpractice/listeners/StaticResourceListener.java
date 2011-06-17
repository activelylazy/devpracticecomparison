package uk.co.activelylazy.devpractice.listeners;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import javax.servlet.http.HttpServletRequest;

import uk.co.activelylazy.devpractice.Response;

public class StaticResourceListener implements RequestListener {

	@Override
	public Response request(HttpServletRequest request) throws IOException {
		File file = new File("src/main/webapp/"+request.getRequestURI());
		StringBuffer buff = new StringBuffer();
		
		LineNumberReader reader = new LineNumberReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			buff.append(line).append("\n");
		}
		
		return new Response(getContentType(request.getRequestURI()), buff.toString());
	}

	private String getContentType(String requestURI) {
		if (requestURI.startsWith("/js")) {
			return "text/javascript";
		} else if (requestURI.endsWith(".html")) {
			return "text/html";
		} else {
			return "text/plain";
		}
	}

}
