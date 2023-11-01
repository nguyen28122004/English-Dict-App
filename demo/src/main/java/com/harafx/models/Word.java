package com.harafx.models;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.harafx.controllers.vocaController;

public class Word {
    private String word;
    private String typeOfWord;
    private String ipa;
    private ArrayList<Explanation> explanations = new ArrayList<Explanation>();

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTypeOfWord() {
        return typeOfWord;
    }

    public void setTypeOfWord(String typeOfWord) {
        this.typeOfWord = typeOfWord;
    }

    public String getIpa() {
        if (ipa.length() > 0) {
            return "/" + ipa + "/";
        }
        return ipa;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    public ArrayList<Explanation> getExplanations() {
        return explanations;
    }

    public void setExplanations(JSONArray explanations, String t) {
        for (Object tmp : explanations) {
            JSONObject jo = (JSONObject) tmp;
            Explanation explanation = new Explanation();

            explanation.setEnglishExplanation((String) jo.get("english_explanation"));
            explanation.setVietnameseExplanation((String) jo.get("vietnamese_explanation"));
            explanation.setExample((String) jo.get("example"));

            this.explanations.add(explanation);
        }
    }

    public void setExplanations(ArrayList<Explanation> explanations2) {
        for (Explanation explanation : explanations2) {
            this.explanations.add(explanation);
        }
    }

    // constructors
    public Word() {
    }

    public Word(Word word2) {
        word = word2.word;
        typeOfWord = word2.typeOfWord;
        ipa = word2.ipa;
    }

    public Word(JSONObject jo) {
        // Asign value to word
        word = (String) jo.get("word");
        ipa = (String) jo.get("ipa");
        typeOfWord = (String) jo.get("type_of_word");
        this.setExplanations((JSONArray) jo.get("explanations"), "");
    }

    // DEBUG FUNC
    public void debug() {
        System.out.println("Word: " + word);
        System.out.println("IPA: " + ipa);
        System.out.println("Type: " + typeOfWord);
        System.out.println("Explain: " + explanations.size());
        for (Explanation explanation : explanations) {
            System.out.println(explanation.getVietnameseExplanation());
            System.out.println(explanation.getEnglishExplanation());
            System.out.println(explanation.getExample());
            System.out.println("------");
        }
        System.out.println("==========================");
    }
}
