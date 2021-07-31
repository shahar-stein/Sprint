package com.example.shaharproject;

public class groupdeatails
{
    private String groupcode;

    private String id;

    private String usern;

    private String startdate;

    private String enddate;

    private String groupgrade;

    private String trainergrade;

    public groupdeatails()
    {

    }

    public String getID() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupcode() { return groupcode; }

    public void setGroupcode(String groupcode) {
        this.groupcode = groupcode;
    }

    public String getUsern() {
        return usern;
    }

    public void setUsern(String usern) {
        this.usern = usern;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getGroupgrade() {
        return groupgrade;
    }

    public void setGroupgrade(String groupgrade) {
        this.groupgrade = groupgrade;
    }

    public String getTrainergrade() {
        return trainergrade;
    }

    public void setTrainergrade(String trainergrade) {
        this.trainergrade = trainergrade;
    }

}
