package com.example.shaharproject;

public class groups
{
    private String groupcode;

    private String price;

    private String id;

    private String opendate;

    private String max;

    private String pic;



    public groups()
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOpendate() {
        return opendate;
    }

    public void setOpendate(String opendate) {
        this.opendate = opendate;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return groupcode+"#"+price+"#"+opendate+"#"+max+"#"+pic;
    }
}
