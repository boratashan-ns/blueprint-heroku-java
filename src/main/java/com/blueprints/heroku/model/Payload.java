package com.blueprints.heroku.model;

import com.google.gson.annotations.SerializedName;

public class Payload{

	@SerializedName("id")
	private String id;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
}