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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

public class vocaController implements Initializable {
    // Normal variables
    Dictionary dict = new Dictionary();
    ObservableList<String> wordList;
    FilteredList<String> filteredWordList;

    // FXML
    @FXML
    private ListView<String> wordsListView = new ListView<>();
    @FXML
    private Label wordArea = new Label();
    @FXML
    private Label ipaArea = new Label();
    @FXML
    private VBox explanationBox = new VBox();
    @FXML
    private TextField searchField = new TextField();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadDictFromJson("dict.json");
        // Push data from dict to wordList\
        wordList = FXCollections.observableArrayList();
        for (int i = 0; i < dict.getSize(); i++) {
            wordList.add(dict.getAllWord(i).getWord());
        }

        // Push wordList to wordListView
        filteredWordList = new FilteredList<String>(wordList);
        wordsListView.getItems().addAll(wordList);

        // Xử lí sự kiện chọn 1 từ trong wordListView
        wordsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == "" || newValue == null || newValue.isEmpty()) {
                    wordArea.setText("");
                    ipaArea.setText("");
                    explanationBox.getChildren().clear();
                    return;
                }
                int curSelectedIndex = wordsListView.getSelectionModel().getSelectedIndex();
                // Search index of word
                for (int i = 0; i < wordList.size(); i++) {
                    if (wordList.get(i) == newValue) {
                        curSelectedIndex = i;
                    }
                }

                wordArea.setText(dict.getAllWord(curSelectedIndex).getWord());
                if (dict.getAllWord(curSelectedIndex).getIpa() == ""
                        || dict.getAllWord(curSelectedIndex).getIpa() == null
                        || dict.getAllWord(curSelectedIndex).getIpa().isEmpty()) {
                    ipaArea.setText("");
                } else
                    ipaArea.setText("/" + dict.getAllWord(curSelectedIndex).getIpa() + "/");

                // Remove all old children
                explanationBox.getChildren().clear();
                // Explanation Box

                ArrayList<Explanation> explanations = dict.getAllWord(curSelectedIndex).getExplanations();
                for (Explanation explanation : explanations) {
                    Label vietnameseExLabel = new Label();
                    Label englishExLabel = new Label();
                    Label exampleLabel = new Label();
                    Separator separator = new Separator();

                    vietnameseExLabel.setText(explanation.getVietnameseExplanation());
                    englishExLabel.setText(explanation.getEnglishExplanation());
                    exampleLabel.setText(explanation.getExample());

                    // Style
                    vietnameseExLabel.setPrefWidth(explanationBox.getWidth() - 10);
                    englishExLabel.setPrefWidth(explanationBox.getWidth() - 10);
                    exampleLabel.setPrefWidth(explanationBox.getWidth() - 10);
                    vietnameseExLabel.setWrapText(true);
                    englishExLabel.setWrapText(true);
                    exampleLabel.setWrapText(true);
                    exampleLabel.setFont(Font.font("Serif", FontPosture.ITALIC, Font.getDefault().getSize()));

                    // Add to layouts
                    explanationBox.getChildren().addAll(vietnameseExLabel, englishExLabel, exampleLabel);
                    explanationBox.getChildren().add(separator);
                    explanationBox.setSpacing(3);

                    // Prepare for passing to Edit Stage
                    Data.passed_word = new Word(dict.getAllWord(curSelectedIndex));
                    Data.passed_word.setExplanations(explanations);
                    Data.index = curSelectedIndex;
                }

                // Style
            }

        });
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

    // Update new word
    private void updateNewWord() {
        dict = new Dictionary();
        loadDictFromJson("dict.json");
        // filteredWordList.clear();
        wordList.clear();
        for (int i = 0; i < dict.getSize(); i++) {
            wordList.add(dict.getAllWord(i).getWord());
        }

        filteredWordList = new FilteredList<String>(wordList);
        wordsListView.getItems().clear();
        wordsListView.getItems().addAll(wordList);
    }

    // Searching function
    public void search(KeyEvent event) {
        filteredWordList.setPredicate(word -> {
            if (searchField.getText() == null || searchField.getText().isEmpty()) {
                return true;
            }

            String keyword = searchField.getText().toLowerCase();
            return word.toLowerCase().contains(keyword);
        });
        wordsListView.getItems().clear();
        wordsListView.getItems().setAll(filteredWordList);
    }

    // Adding function
    public void add(ActionEvent event) throws IOException {
        Stage addWordStage = new Stage();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/add_word.fxml"));
        addWordStage.setTitle("English Vocabulary Apps - Add Word");
        addWordStage.setScene(new Scene(root));
        addWordStage.setResizable(false);
        addWordStage.showAndWait();

        // Update new word
        updateNewWord();
    }

    // Editing function
    public void edit(ActionEvent event) throws IOException {
        // passing dict
        Data.passed_dict = dict;
        // Create Stage
        Stage addEditStage = new Stage();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/edit_word.fxml"));
        addEditStage.setTitle("English Vocabulary Apps - Edit Word");
        addEditStage.setScene(new Scene(root));
        addEditStage.setResizable(false);
        addEditStage.setUserData(dict);
        addEditStage.showAndWait();
        // Update new word
        updateNewWord();
    }

    // Deleting function
    public void delete(ActionEvent event)
            throws UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {
        String curSelection = wordsListView.getSelectionModel().getSelectedItem();
        wordsListView.getSelectionModel().clearSelection();
        Alert alert = new Alert(AlertType.CONFIRMATION, "Click Ok Button for deleting", ButtonType.YES,
                ButtonType.CANCEL);
        alert.setHeaderText("Do you want to delete the word \"" + curSelection + "\"?");
        alert.showAndWait();

        if (alert.getResult().getButtonData() == ButtonData.YES) {
            dict.removeWord(curSelection);
            dict.exportJson("dict.json");
            updateNewWord();
        }
    }
}
