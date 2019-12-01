package com.example.signzyinternshala.Model;


public class UserModelProfileData {

    private String stringUserName, stringRepositoryCount, stringUserProfileURl, stringActualPosition, stringRepoLink;


    public UserModelProfileData(String stringUserName, String stringRepositoryCount, String stringUserProfileURl, String stringActualPosition,String stringRepoLink) {
        this.stringUserName = stringUserName;
        this.stringRepositoryCount = stringRepositoryCount;
        this.stringUserProfileURl = stringUserProfileURl;
        this.stringActualPosition = stringActualPosition;
        this.stringRepoLink = stringRepoLink;
    }

    public UserModelProfileData(String stringUserName, String stringUserProfileURl, String stringActualPosition, String stringRepoLink) {
        this.stringUserName = stringUserName;
        this.stringActualPosition = stringActualPosition;
        this.stringUserProfileURl = stringUserProfileURl;
        this.stringRepoLink = stringRepoLink;
    }

    public String getStringActualPosition() {
        return stringActualPosition;
    }

    public String getStringUserName() {
        return stringUserName;
    }

    public String getStringRepositoryCount() {
        return stringRepositoryCount;
    }

    public String getStringUserProfileURl() {
        return stringUserProfileURl;
    }

    public String getStringRepoLink() {
        return stringRepoLink;
    }
}
