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
import javafx.stage.Stage;

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
            e.printStackTrace();
        }
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

    // Add word
    public void addWord(ActionEvent event)
            throws UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {
        // Check if no word filled
        if (word.getText().length() == 0) {
            Alert alert = new Alert(AlertType.INFORMATION, "Please comeback and fill all the property", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Confirmation
        Alert alert = new Alert(AlertType.CONFIRMATION, "Click Yes button for confirmation.", ButtonType.YES,
                ButtonType.CANCEL);
        alert.setHeaderText("You want to add the word \"" + word.getText() + "\" to the dictionary?");
        alert.setTitle("Add word");
        alert.showAndWait();

        if (alert.getResult().getButtonData() != ButtonData.YES) {
            return;
        }
        // set value for all
        if (alert.getResult() == ButtonType.YES) {
            wordAllProps.setWord(word.getText());
            wordAllProps.setIpa(ipa.getText());
            wordAllProps.setTypeOfWord(typeOfWord.getSelectionModel().getSelectedItem());
            wordAllProps.setExplanations(new ArrayList<Explanation>(explanations));
            dict.removeWord(word.getText());
            dict.addWord(wordAllProps);
            dict.exportJson("dict.json");
            Stage stage = (Stage) word.getScene().getWindow();
            stage.close();
        }
        closeStage(event);
        alert.setAlertType(AlertType.INFORMATION);
        alert.setHeaderText("Your word is added!\nReload the application for update your new word.");
        alert.setContentText("Click OK to continue");
        alert.showAndWait();
    }

    // Cancel
    public void closeStage(ActionEvent event) {
        Stage currentStage = (Stage) word.getScene().getWindow();
        currentStage.close();
    }
}
