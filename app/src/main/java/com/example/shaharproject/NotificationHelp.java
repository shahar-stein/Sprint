package com.example.shaharproject;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationHelp
{
        public static final String ACTION_PLAY="ACTION_PLAY";

        public static final String ACTION_PAUSE="ACTION_PAUSE";
//---------------------------------------------------------

        // Essa funcao cria Pending para Abrir a Activity da Intent

        private static PendingIntent GetPendingIntent(Context ctx, Intent intent, int id)
        {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

            //Mantem a Activity pai na pilha de Activites
            stackBuilder.addParentStack(intent.getComponent());

            // Configura a intent que vai abrir ao clicar o botao
            stackBuilder.addNextIntent(intent);

            // Cria a PendingIntent

            PendingIntent p = stackBuilder.getPendingIntent(id,PendingIntent.FLAG_UPDATE_CURRENT);

            return  p;
        }

        //-----------------------------------------------

        public static void CreateNotification(Context ctx,Intent intent,String contentTitle,String myText,int id)
        {
            // Cria a PendingIntent(contem a Intent Original)

            // id serve para cancelar a notificacao manualmente

            PendingIntent p =GetPendingIntent(ctx,intent,id);

            NotificationCompat.Builder builder=new NotificationCompat.Builder(ctx);

            builder.setDefaults(Notification.DEFAULT_ALL); // Ativa a configuracao padrao

            builder.setSmallIcon(R.drawable.ic_launcher_background);

            builder.setContentTitle(contentTitle);

            builder.setContentText(myText);

            builder.setContentIntent(p); // intent que era chamado ao clicar na notificacao

            builder.setAutoCancel(true);//cancela a notificacao ao clicar na tela

            NotificationManagerCompat nm = NotificationManagerCompat.from(ctx);

            nm.notify(id, builder.build());
        }

        //-----------------------------------------------
        public static void CreateHeadUpNotification(Context ctx,Intent intent,String contentTitle,String myText,int id)
        {
            PendingIntent p =GetPendingIntent(ctx,intent,id);

            NotificationCompat.Builder builder=new NotificationCompat.Builder(ctx);

            builder.setDefaults(Notification.DEFAULT_ALL); // Ativa a configuracao padrao

            builder.setSmallIcon(R.drawable.loading);

            builder.setColor(Color.WHITE); //Extra

            builder.setFullScreenIntent(p,false);//extra

            builder.setContentTitle(contentTitle);

            builder.setContentText(myText);

            builder.setContentIntent(p); // intent que era chamado ao clicar na notificacao

            builder.setAutoCancel(true);//cancela a notificacao ao clicar na tela

            NotificationManagerCompat nm = NotificationManagerCompat.from(ctx);

            nm.notify(id, builder.build());
        }

        //---------------------------------------------------------

        public static void CreateAction(Context ctx,Intent intent,String contentTitle,String myText,int id)
        {

            PendingIntent p =GetPendingIntent(ctx,intent,id);

            NotificationCompat.Builder builder=new NotificationCompat.Builder(ctx);

            builder.setDefaults(Notification.DEFAULT_ALL); // Ativa a configuracao padrao

            builder.setSmallIcon(R.drawable.ic_launcher_background);

            builder.setContentTitle(contentTitle);

            builder.setContentText(myText);

            builder.setAutoCancel(true);//cancela a notificacao ao clicar na tela

            builder.setFullScreenIntent(p,false);//extra

            builder.setContentIntent(p); // intent que era chamado ao clicar na notificacao

            PendingIntent actionIntentplay=PendingIntent.getBroadcast(ctx,0,new Intent(ACTION_PLAY),0);

            PendingIntent actionIntentpause=PendingIntent.getBroadcast(ctx,0,new Intent(ACTION_PAUSE),0);

            builder.addAction(R.drawable.play,"Play",actionIntentplay);

            builder.addAction(R.drawable.pause,"Pause",actionIntentpause);

            NotificationManagerCompat mn = NotificationManagerCompat.from(ctx);

            mn.notify(id,builder.build());

        }
        //---------------------------------------------------------
        public static void Cancel(Context ctx,int id)
        {
            NotificationManagerCompat mn = NotificationManagerCompat.from(ctx);

            mn.cancel(id);
        }
        //---------------------------------------------------------
        public static void CancelAll(Context ctx)
        {
            NotificationManagerCompat mn = NotificationManagerCompat.from(ctx);

            mn.cancelAll();
        }
    }

