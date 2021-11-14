package model;

import play.data.validation.Constraints;
public class KeywordModel {

	@Constraints.Required
    protected String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}
