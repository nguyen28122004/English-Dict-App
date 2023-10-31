package com.harafx.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.simple.parser.ParseException;

import com.harafx.models.Dictionary;
import com.harafx.models.Explanation;
import com.harafx.models.Word;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class addWordController implements Initializable {

    Dictionary dict = new Dictionary();
    Word wordAllProps = new Word();
    ArrayList<Explanation> explanations = new ArrayList<>();
    Explanation explanation = new Explanation();

    @FXML
    TextField word = new TextField();
    @FXML
    TextField ipa = new TextField();
    @FXML
    TextField vietnameseExplanation = new TextField();
    @FXML
    TextField englishExplanation = new TextField();
    @FXML
    TextField example = new TextField();
    @FXML
    ChoiceBox<String> typeOfWord = new ChoiceBox<>();

    @FXML
    ChoiceBox<Integer> explMenu = new ChoiceBox<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDictFromJson("dict.json");
        addTypeOfWord();
        explMenu.getItems().add(1);
        explMenu.setValue(1);

        explMenu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
                if (arg2 != explanations.size() + 1) {
                    vietnameseExplanation.setText(explanations.get(arg2 - 1).getVietnameseExplanation());
                    englishExplanation.setText(explanations.get(arg2 - 1).getEnglishExplanation());
                    example.setText(explanations.get(arg2 - 1).getExample());
                } else {
                    vietnameseExplanation.setText("");
                    englishExplanation.setText("");
                    example.setText("");
                }
            }

        });
    }

    // Add type of word
    private void addTypeOfWord() {
        String[] typeList = { "adjectives", "adverbs", "conjunctions", "determiners", "nouns", "prepositions",
                "pronouns", "verbs" };
        typeOfWord.getItems().addAll(typeList);
    }

    // Load from file
    public void loadDictFromJson(String path) {
        // Get data from dict.json
        try {
            dict.loadJson(path);
        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Export to file
    public void exportDictToJson(String path) {
        try {
            dict.exportJson(path);
        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Add explanation
    public void addExplanation() {
        explanation.setEnglishExplanation(englishExplanation.getText());
        explanation.setVietnameseExplanation(vietnameseExplanation.getText());
        explanation.setExample(example.getText());

        englishExplanation.setText("");
        vietnameseExplanation.setText("");
        example.setText("");

        explanations.add(new Explanation(explanation));

        explMenu.getItems().add(explanations.size() + 1);
        explMenu.setValue(explanations.size() + 1);

    }

    // Add word
    public void addWord(ActionEvent event)
            throws UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {
        addExplanation();
        // set value for all
        wordAllProps.setWord(word.getText());
        wordAllProps.setIpa(ipa.getText());
        wordAllProps.setTypeOfWord(typeOfWord.getValue());
        wordAllProps.setExplanations(explanations);
        dict.addWord(wordAllProps);
        dict.exportJson("dict1.json");
    }
}
