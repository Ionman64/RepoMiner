package org.repominer.com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIHandler {
	private String apiKey = null;
	private String user = null;
	private String repo = null;
	public APIHandler(String apiKey, String user, String repo) {
		this.apiKey = apiKey;
		this.user = user;
		this.repo = repo;
	}
	public String getCommits() {
		String url = "https://api.github.com/repos/:owner/:repo/git/commits";
		url = url.replace(":owner", this.user);
		url = url.replace(":repo", this.repo);
		
		return sendPostRequest(url, null);
		
	}
	public static String sendPostRequest(String requestUrl, String payload) {
		StringBuffer jsonString = new StringBuffer();
	    try {
	        URL url = new URL(requestUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        //connection.setDoInput(true);
	        //connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        //OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	        //writer.write(payload);
	        //writer.close();
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line;
	        while ((line = br.readLine()) != null) {
	                jsonString.append(line);
	        }
	        br.close();
	        connection.disconnect();
	    } catch (Exception e) {
	    	e.printStackTrace();
	            throw new RuntimeException(e.getMessage());
	    }
	    return jsonString.toString();
	}
}
