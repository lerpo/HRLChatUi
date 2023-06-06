package com.hrl.chaui.bean.gpt;

import java.util.List;

public class GptMessageBody {
   private String id;
   private String model;
   private String object;
   private List<Choices> choices;
   private Useage usage;
   private String conversation_id;
   private String message_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Choices> getChoices() {
        return choices;
    }

    public void setChoices(List<Choices> choices) {
        this.choices = choices;
    }

    public Useage getUsage() {
        return usage;
    }

    public void setUsage(Useage usage) {
        this.usage = usage;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }
}




