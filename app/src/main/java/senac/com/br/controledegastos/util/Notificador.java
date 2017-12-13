package senac.com.br.controledegastos.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import senac.com.br.controledegastos.R;

// Created by Carlos Lohmeyer on 06/12/2017.

public class Notificador extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static int NOTIFICATION_ID_VALUE = 199;
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        int acao = intent.getExtras().getInt("acao");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        switch (acao){
            case Constantes.BROADCAST_EXECUTAR_ACAO:
                int idNotif = intent.getIntExtra("idNotificacao", 0);
                notificationManager.cancel(idNotif);
                Toast.makeText(context, context.getResources().getString(R.string.toast_reciever), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}