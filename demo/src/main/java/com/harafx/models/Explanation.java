package com.harafx.models;

public class Explanation {
    private String englishExplanation;
    private String vietnameseExplanation;
    private String example;

    public Explanation() {
    }

    public Explanation(Explanation explanation) {
        this.englishExplanation = explanation.englishExplanation;
        this.vietnameseExplanation = explanation.vietnameseExplanation;
        this.example = explanation.example;
    }

    public String getEnglishExplanation() {
        return englishExplanation;
    }

    public void setEnglishExplanation(String englishExplanation) {
        this.englishExplanation = englishExplanation;
    }

    public String getVietnameseExplanation() {
        return vietnameseExplanation;
    }

    public void setVietnameseExplanation(String vietnameseExplanation) {
        this.vietnameseExplanation = vietnameseExplanation;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
