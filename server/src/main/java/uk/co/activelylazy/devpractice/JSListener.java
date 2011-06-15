package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import javax.servlet.http.HttpServletRequest;

public class JSListener implements RequestListener {

	@Override
	public String request(HttpServletRequest request) throws IOException {
		InputStream in = getClass().getResourceAsStream(request.getRequestURI());
		StringBuffer buff = new StringBuffer();
		
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			buff.append(line).append("\n");
		}
		
		return buff.toString();
	}

}
