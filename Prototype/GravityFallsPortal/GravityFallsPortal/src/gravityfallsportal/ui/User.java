/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravityfallsportal.ui;

/**
 *
 * @author mehmet
 */
public class User {
    
    private String UserName;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private int wins;
    private int highscore;
    
    public int getHighscore(){
        return highscore;
    }
    
    public void setHighscore(int Highscore){
        this.highscore = Highscore;
    }
    
    public int getWins(){
        return wins;
    }
    
    public void setWins(int Wins) {
        this.wins = Wins;
    }
    
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
    
}
