package com.example.demo.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("feedback")
public class Feedback {
    @Id
    private String id;
    private String feedbacktxt;

    public Feedback(String id, String feedbacktxt) {
        super();
        this.id = id;
        this.feedbacktxt = feedbacktxt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedbacktxt() {
        return feedbacktxt;
    }

    public void setFeedbacktxt(String feedbacktxt) {
        this.feedbacktxt = feedbacktxt;
    }
}
