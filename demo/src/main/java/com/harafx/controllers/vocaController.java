package com.harafx.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.simple.parser.ParseException;

import com.harafx.models.Dictionary;
import com.harafx.models.Explanation;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
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
                int curSelectedIndex = wordsListView.getSelectionModel().getSelectedIndex();

                wordArea.setText(dict.getAllWord(curSelectedIndex).getWord());
                ipaArea.setText(dict.getAllWord(curSelectedIndex).getIpa());

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
        addWordStage.show();

    }
}
