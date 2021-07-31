package com.example.shaharproject;

public class competitiondeatails
{

    private String id;

    private String competitionname;

    private String traineecode;

    private String traineestatus;

    private String results;

    private String registerdate;

    private String length;

    public competitiondeatails()
    {

    }

    public String getID() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompetitionname() { return competitionname; }

    public void setCompetitionname(String competitionname) {
        this.competitionname = competitionname;
    }

    public String getTraineecode() { return traineecode; }

    public void setTraineecode(String traineecode) {
        this.traineecode = traineecode;
    }

    public String getTraineestatus() {
        return traineestatus;
    }

    public void setTraineestatus(String traineestatus) {
        this.traineestatus = traineestatus;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(String registerdate) {
        this.registerdate = registerdate;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return id+"#"+competitionname+"#"+traineecode+"#"+traineestatus+"#"+results+"#"+registerdate+"#"+length;
    }
}
