package com.example.shaharproject;

public class competitions
{

    private String id;

    private String competitionname;

    private String trainercode;

    private String adress;

    private String description;

    private String competitiondate;

    private String competitionhour;

    private String logo;

    private String competitionstatus;



    public competitions()
    {

    }

    public String getID() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompetitionname() {
        return competitionname;
    }

    public void setCompetitionname(String competitionname) {
        this.competitionname = competitionname;
    }

    public String getTrainercode() {
        return trainercode;
    }

    public void setTrainercode(String trainercode) {
        this.trainercode = trainercode;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompetitiondate() {return competitiondate;};

    public void setCompetitiondate(String competitiondate) {this.competitiondate=competitiondate;}

    public String getCompetitionhour() {
        return competitionhour;
    }

    public void setCompetitionhour(String competitionhour) {
        this.competitionhour = competitionhour;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCompetitionstatus() { return competitionstatus; }

    public void setCompetitionstatus(String competitionstatus) {
        this.competitionstatus = competitionstatus;
    }

    @Override
    public String toString()
    {
        return competitionname+"#"+trainercode+"#"+adress+"#"+description+"#"+competitiondate+"#"+competitionhour+"#"+logo;
    }

}
