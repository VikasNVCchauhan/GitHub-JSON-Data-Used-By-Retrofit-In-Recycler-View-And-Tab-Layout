package com.example.signzyinternshala.Model;

public class UserModelRepositoryData {
    private String repositoryName, stringLanguage, stringScore, stringWatcherCount, stringOpenIssues, stringForkCount;

    public UserModelRepositoryData(String repositoryName, String stringLanguage, String stringScore, String stringWatcherCount, String stringOpenIssues, String stringForkCount) {
        this.repositoryName = repositoryName;
        this.stringLanguage = stringLanguage;
        this.stringScore = stringScore;
        this.stringWatcherCount = stringWatcherCount;
        this.stringOpenIssues = stringOpenIssues;
        this.stringForkCount = stringForkCount;
    }

    public UserModelRepositoryData() {

    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getStringLanguage() {
        return stringLanguage;
    }

    public String getStringScore() {
        return stringScore;
    }

    public String getStringWatcherCount() {
        return stringWatcherCount;
    }

    public String getStringOpenIssues() {
        return stringOpenIssues;
    }

    public String getStringForkCount() {
        return stringForkCount;
    }
}
