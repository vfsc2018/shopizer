package com.salesmanager.shop.store.security;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationPush{
	
	private String app = "com.vifotec.vfscfood";
	private List<String> fcmTokens;
	
	private String topic;

	private String title;
	private String body;
	private Integer sync;
	private String data;
	private String image;
	private String action;
	
	public List<String> getFcmTokens() {
		return fcmTokens;
	}
	public void setFcmTokens(List<String> fcmTokens) {
		this.fcmTokens = fcmTokens;
	}

	public Integer getSync() {
		return sync;
	}
	public void setSync(Integer sync) {
		this.sync = sync;
	}

	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
	}
	
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
	}
	
	public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public NotificationPush() {
        
	}
	
	public NotificationPush(String topic) {
		this.topic = topic;
    }



}
