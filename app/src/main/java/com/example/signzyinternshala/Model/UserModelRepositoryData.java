package com.example.signzyinternshala.Model;

public class UserModelRepositoryData implements Comparable<UserModelRepositoryData> {
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

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public void setStringLanguage(String stringLanguage) {
        this.stringLanguage = stringLanguage;
    }

    public void setStringScore(String stringScore) {
        this.stringScore = stringScore;
    }

    public void setStringWatcherCount(String stringWatcherCount) {
        this.stringWatcherCount = stringWatcherCount;
    }

    public void setStringOpenIssues(String stringOpenIssues) {
        this.stringOpenIssues = stringOpenIssues;
    }

    public void setStringForkCount(String stringForkCount) {
        this.stringForkCount = stringForkCount;
    }

    @Override
    public int compareTo(UserModelRepositoryData o) {
        return Integer.valueOf(this.getStringScore()).compareTo(Integer.valueOf(o.getStringScore()));
    }
}
