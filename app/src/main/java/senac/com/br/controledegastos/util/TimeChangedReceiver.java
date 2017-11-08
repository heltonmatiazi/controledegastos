package senac.com.br.controledegastos.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import senac.com.br.controledegastos.activities.AvisoActivity;
import senac.com.br.controledegastos.activities.TutorialActivity;

/**
 * Created by helton on 06/11/2017.
 */

public class TimeChangedReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        //para o ambiente de testes
        Toast.makeText(context, "time change " + intent.getAction(), Toast.LENGTH_SHORT).show();
        // essa activity vai mostrar um warning para o usuário caso a data do dispositivo seja alterada manualmente
        // ou por mudança de timezone
        context.startActivity(new Intent(context, AvisoActivity.class));
    }

}
