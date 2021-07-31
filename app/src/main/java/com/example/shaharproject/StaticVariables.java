package com.example.shaharproject;

public class StaticVariables
{
    private  static tblclients myclient=null;

    private static trainers mytrainer=null;

    private static trainee mytrainee=null;

    private static groups mygroup=null;

    private static competitions mycompetition=null;

    public static competitiondeatails mycompetitiondeatails=null;

    private static groupdeatails currentgroup=null;

    private static String music="";

    public static void setMusic(String myMusic)
    {
        StaticVariables.music=myMusic;
    }

    public static String GetMyMusic()
    {
        return  StaticVariables.music;
    }


    public static void SetClient(tblclients c)
    {
        StaticVariables.myclient=c;
    }

    public static tblclients GetClient()
    {
        return StaticVariables.myclient;
    }

    public static void SetTrainer(trainers t)
    {
        StaticVariables.mytrainer=t;
    }

    public static trainers GetTrainer()
    {
        return StaticVariables.mytrainer;
    }

    public static void SetTrainee(trainee tr)
    {
        StaticVariables.mytrainee=tr;
    }

    public static trainee GetTrainee()
    {
        return StaticVariables.mytrainee;
    }

    public static void SetGroup(groups gr) {StaticVariables.mygroup=gr;}

    public static groups GetGroup() {return StaticVariables.mygroup;}

    public static void SetCurrentGroup(groupdeatails gd) {StaticVariables.currentgroup=gd;}

    public static groupdeatails GetCurrentGroup() {return StaticVariables.currentgroup;}

    public static void setMycompetition(competitions c)
    {
        StaticVariables.mycompetition=c;
    }

    public static competitions getMycompetition()
    {
        return StaticVariables.mycompetition;
    }

    public static void setMycompetitiondeatails(competitiondeatails c)
    {
        StaticVariables.mycompetitiondeatails=c;
    }

    public static competitiondeatails getMycompetitiondeatails()
    {
        return StaticVariables.mycompetitiondeatails;
    }



}
