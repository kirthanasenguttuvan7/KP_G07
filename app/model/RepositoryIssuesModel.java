package model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RepositoryIssuesModel{
	 private String issue_number;
	 private String issue_title;
	 private String state ;
	 private String created_at;
	 private String updated_at;
	 
	public RepositoryIssuesModel(String issue_number, String issue_title, String state, String created_at,
			String updated_at) {
		this.issue_number = issue_number;
		this.issue_title = issue_title;
		this.state = state;
		this.created_at = created_at;
		this.updated_at = updated_at;
		
	}
	public String getIssue_title() {
		return this.issue_title;
	}
	
}




