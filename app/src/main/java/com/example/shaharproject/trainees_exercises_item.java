package com.example.shaharproject;

public class trainees_exercises_item
{
    private boolean checked = false;

    private String itemText = "";

    //private String img ="";

    public boolean isCheckes()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked=checked;
    }

    public String getItemText()
    {
        return itemText;
    }

    public void setItemText(String itemText)
    {
        this.itemText=itemText;
    }

    /*public String getImg()
    {
        return this.img;
    }

    public void setImg(String img)
    {
        this.img=img;
    }*/
}
