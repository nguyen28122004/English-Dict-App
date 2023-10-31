package com.harafx.models;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Dictionary {
    private ArrayList<Word> words = new ArrayList<>();
    private JSONArray dictJa = new JSONArray();

    public Dictionary() {
    }

    public JSONArray getDictJa() {
        return dictJa;
    }

    public void setDictJa(JSONArray dictJa) {
        this.dictJa = dictJa;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public int getSize() {
        return words.size();
    }

    public Word getAllWord(int i) {
        return this.words.get(i);
    }

    // Add word to dict
    public void addWord(Word word) {
        JSONObject jo = new JSONObject();

        this.words.add(word);
        jo.put("word", word.getWord());
        jo.put("ipa", word.getIpa());
        jo.put("type_of_word", word.getTypeOfWord());

        JSONArray explJArray = new JSONArray();
        JSONObject explJObject = new JSONObject();

        for (Explanation explanation : word.getExplanations()) {
            explJObject.put("vietnamese_explanation", explanation.getVietnameseExplanation());
            explJObject.put("english_explanation", explanation.getEnglishExplanation());
            explJObject.put("example", explanation.getExample());

            explJArray.add(explJObject);
        }

        jo.put("explanation", explJArray);
        System.out.println(dictJa.size());
        dictJa.add(jo);

        System.out.println(dictJa.size());
        // dictJa.add((JSONObject) word);
    }

    public void loadJson(String path) throws FileNotFoundException, IOException, ParseException {

        // parsing file "JSONExample.json"
        Object obj = new JSONParser().parse(new BufferedReader(new InputStreamReader(
                new FileInputStream(path), "UTF-8")));

        // typecasting obj to JSONObject
        JSONArray ja = (JSONArray) obj; // JSONArray
        dictJa = ja;
        JSONObject jo = new JSONObject(); // JSONObject

        // cast JSONArray to ArrayList<Word>
        for (Object tmp : ja) {
            jo = (JSONObject) tmp;
            Word word = new Word(jo);
            this.words.add(word);
        }
    }

    public void exportJson(String path)
            throws UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {

        // writing JSON to file
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8),
                true);
        pw.write(dictJa.toJSONString());

        pw.flush();
        pw.close();
    }
}
