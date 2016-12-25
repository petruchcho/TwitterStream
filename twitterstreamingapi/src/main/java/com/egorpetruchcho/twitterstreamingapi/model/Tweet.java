package com.egorpetruchcho.twitterstreamingapi.model;

public class Tweet {
	
	private String text;

	private User user;

	public String getText() {
		return text;
	}

	void setText(String text) {
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	void setUser(User user) {
		this.user = user;
	}
}
