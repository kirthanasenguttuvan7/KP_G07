package model;

import java.util.ArrayList;

public class ContributionsModel {
	private String login;
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	
	
	public int getContributions() {
		return contributions;
	}
	public void setContributions(int contributions) {
		this.contributions = contributions;
	}

	private String avatar_url;
	private int contributions;
	
	

}
