package com.home.englishnote.models.repositories;

import com.home.englishnote.models.entities.Word;
import com.home.englishnote.models.entities.WordGroup;

import java.util.ArrayList;
import java.util.List;

public class StubWordGroupRepository implements WordGroupRepository {
    @Override
    public WordGroup getWordGroup(int wordGroupId) {
        return new WordGroup(0, "title", new ArrayList<>());
    }

    @Override
    public List<WordGroup> getWordGroupsFromPublicDictionary(int dictionaryId, int offset, int limit) {
        List<WordGroup> wordGroupList = new ArrayList<>(1);
        List<Word> wordList = new ArrayList<>(3);
        wordList.add(new Word(0, "wordName1", "wordSynonym1", "wordImage1"));
        wordList.add(new Word(1, "wordName2", "wordSynonym2", "wordImage2"));
        wordList.add(new Word(2, "wordName3", "wordSynonym3", "wordImage3"));
        wordList.add(new Word(3, "wordName4", "wordSynonym4", "wordImage4"));
        wordList.add(new Word(4, "wordName5", "wordSynonym5", "wordImage5"));
        wordList.add(new Word(5, "wordName6", "wordSynonym6", "wordImage6"));
        wordGroupList.add(new WordGroup(0, "title1", wordList));
        wordGroupList.add(new WordGroup(1, "title2", wordList));
        return wordGroupList;
    }

    @Override
    public List<WordGroup> getWordGroupsFromOwnDictionary(int memberId, int offset, int limit) {
        return null;
    }
}
