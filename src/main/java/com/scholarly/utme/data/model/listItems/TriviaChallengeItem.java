package com.scholarly.utme.data.model.listItems;

public class TriviaChallengeItem {

    private String challengeImageUrl;
    private String topic;
    private String subTopic;
    private String numOfPlayers;
    private String numOfQuestions;
    private String playerName;
    private String playerImageUrl;
    private int challengeStatus;

    public TriviaChallengeItem(String challengeImageUrl, String topic, String subTopic, String numOfPlayers, String numOfQuestions, String playerName, String playerImageUrl, int challengeStatus) {
        this.challengeImageUrl = challengeImageUrl;
        this.topic = topic;
        this.subTopic = subTopic;
        this.numOfPlayers = numOfPlayers;
        this.numOfQuestions = numOfQuestions;
        this.playerName = playerName;
        this.playerImageUrl = playerImageUrl;
        this.challengeStatus = challengeStatus;
    }

    public String getChallengeImageUrl() {
        return challengeImageUrl;
    }

    public String getTopic() {
        return topic;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public String getNumOfPlayers() {
        return numOfPlayers;
    }

    public String getNumOfQuestions() {
        return numOfQuestions;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerImageUrl() {
        return playerImageUrl;
    }

    public int getChallengeStatus() {
        return challengeStatus;
    }
}
