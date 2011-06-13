package uk.co.activelylazy.devpractice;

import javax.servlet.http.HttpServletRequest;

public class PingListener implements RequestListener {

	@Override
	public String request(HttpServletRequest request) {
		return "Server OK";
	}

}
