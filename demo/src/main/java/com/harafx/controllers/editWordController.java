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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class editWordController implements Initializable {

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
        // Get passed data
        dict = Data.passed_dict;
        wordAllProps = Data.passed_word;

        setTypeOfWord();
        setExplanations();
        setWord();
        setIpa();
        explMenu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1,
                    Integer arg2) {
                if (arg2 != explanations.size() + 1) {
                    vietnameseExplanation.setText(explanations.get(arg2 -
                            1).getVietnameseExplanation());
                    englishExplanation.setText(explanations.get(arg2 -
                            1).getEnglishExplanation());
                    example.setText(explanations.get(arg2 - 1).getExample());
                } else {
                    vietnameseExplanation.setText("");
                    englishExplanation.setText("");
                    example.setText("");
                }
            }

        });
    }

    // Set current value
    private void setTypeOfWord() {
        String[] typeList = { "adjective", "adverb", "conjunction", "determiner", "noun", "preposition",
                "pronoun", "verb", "indefinite article" };
        typeOfWord.getItems().addAll(typeList);

        if (wordAllProps.getTypeOfWord() == null || wordAllProps.getTypeOfWord().length() == 0
                || wordAllProps.getTypeOfWord() == "") {
            return;
        } else {
            typeOfWord.getSelectionModel().select(wordAllProps.getTypeOfWord());
        }
    }

    private void setExplanations() {
        explanations = wordAllProps.getExplanations();
        if (explanations.size() == 0) {
            explMenu.getItems().add(1);
            explMenu.getSelectionModel().select(0);
            return;
        }

        for (int i = 0; i < explanations.size(); i++) {
            explMenu.getItems().add(i + 1);
        }
        explMenu.getSelectionModel().select(0);
        vietnameseExplanation.setText(explanations.get(0).getVietnameseExplanation());
        englishExplanation.setText(explanations.get(0).getEnglishExplanation());
        example.setText(explanations.get(0).getExample());

    }

    private void setWord() {
        word.setText(wordAllProps.getWord());
    }

    private void setIpa() {
        if (wordAllProps.getIpa() == "" || wordAllProps.getIpa() == null || wordAllProps.getIpa().isEmpty()) {
            return;
        }
        ipa.setText(wordAllProps.getIpa());
    }

    // Export to file
    public void exportDictToJson(String path) {
        try {
            dict.exportJson(path);
        } catch (IOException | ParseException e) {
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

        explMenu.getItems().add(explanations.size() + 1);
        explMenu.setValue(explanations.size() + 1);

    }

    // Change Explanation
    public void changeExplanation() {
        explanation.setEnglishExplanation(englishExplanation.getText());
        explanation.setVietnameseExplanation(vietnameseExplanation.getText());
        explanation.setExample(example.getText());

        int curEx = explMenu.getSelectionModel().getSelectedIndex();
        if (curEx < explanations.size()) {
            explanations.set(curEx, new Explanation(explanation));
        } else if (curEx == explanations.size()) {
            explanations.add(new Explanation(explanation));
        }
    }

    // Edit word
    public void editWord(ActionEvent event)
            throws UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {
        // Confirmation
        Alert alert = new Alert(AlertType.CONFIRMATION, "Click Yes for confirmation", ButtonType.YES,
                ButtonType.CANCEL);
        alert.setHeaderText("Do you want to apply this edit to the word \"" + word.getText() + "\"?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            wordAllProps.setIpa(ipa.getText());
            wordAllProps.setTypeOfWord(typeOfWord.getSelectionModel().getSelectedItem());
            wordAllProps.setExplanations(new ArrayList<Explanation>(explanations));
            dict.removeWord(word.getText());
            dict.addWord(wordAllProps);
            dict.exportJson("dict.json");
            Stage stage = (Stage) word.getScene().getWindow();
            stage.close();
            alert.setAlertType(AlertType.INFORMATION);
            alert.setHeaderText("Your change is applied!\nReload the application for update your new word.");
            alert.setContentText("Click OK to continue");
            alert.showAndWait();
        }
    }

    // Cancel
    public void closeStage(ActionEvent event) {
        Stage currentStage = (Stage) word.getScene().getWindow();
        currentStage.close();
    }
}
