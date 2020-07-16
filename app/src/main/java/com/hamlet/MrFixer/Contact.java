package com.hamlet.MrFixer;

public class Contact {
    private String FirstName;
    private String LastName;
    private String email;
    private String message;

    public Contact(String firstName, String lastName, String email, String message) {
        FirstName = firstName;
        LastName = lastName;
        this.email = email;
        this.message = message;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
