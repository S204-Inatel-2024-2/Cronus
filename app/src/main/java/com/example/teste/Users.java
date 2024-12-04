package com.example.teste;

public class Users
{
    String email;
    String userName;

    public Users()
    {

    }
    public Users(String email, String userName)
    {
        this.email = email;
        this.userName = userName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
