package com.blueprints.heroku.model;

import com.google.gson.annotations.SerializedName;

public class EventStreamPayload{

	@SerializedName("payload")
	private Payload payload;

	@SerializedName("name")
	private String name;

	@SerializedName("published_at")
	private String publishedAt;

	@SerializedName("tenant")
	private String tenant;

	public void setPayload(Payload payload){
		this.payload = payload;
	}

	public Payload getPayload(){
		return payload;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPublishedAt(String publishedAt){
		this.publishedAt = publishedAt;
	}

	public String getPublishedAt(){
		return publishedAt;
	}

	public void setTenant(String tenant){
		this.tenant = tenant;
	}

	public String getTenant(){
		return tenant;
	}
}