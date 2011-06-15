package uk.co.activelylazy.devpractice;

public class Response {

	private String content;
	private String contentType;
	
	public Response(String contentType, String content) {
		this.contentType = contentType;
		this.content = content;
	}

	public String getContent() { return content; }
	public String getContentType() { return contentType; }

	public static Response plainText(String content) {
		return new Response("text/plain", content);
	}
	
}
