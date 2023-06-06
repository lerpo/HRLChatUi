package com.hrl.chaui.bean;

public class ConversationMsgBody {
    private String conversationId;
    private String createDate;
    private String id;
    private String message;
    private String role;
    private String usedKey;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsedKey() {
        return usedKey;
    }

    public void setUsedKey(String usedKey) {
        this.usedKey = usedKey;
    }
}
