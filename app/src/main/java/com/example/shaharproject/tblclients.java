package com.example.shaharproject;

public class tblclients
{
    private String id;

    private String usern;

    private String pass;

    private String firstn;

    private String lastn;

    private String gender;

    private String dateborn;

    private String email;

    private String phone;

    private String pic;

    private String roll;


    public tblclients()
    {

    }

    public String getID() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsern() { return usern; }

    public void setUsern(String usern) {
        this.usern = usern;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFirstn() {
        return firstn;
    }

    public void setFirstn(String firstn) {
        this.firstn = firstn;
    }

    public String getLastn() {
        return lastn;
    }

    public void setLastn(String lastn) {
        this.lastn = lastn;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateborn() { return dateborn; }

    public void setDateborn(String dateborn) {
        this.dateborn = dateborn;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String toString()
    {
        return firstn+"#"+lastn+"#"+gender+"#"+phone+"#"+email+"#"+pic;
    }
}
