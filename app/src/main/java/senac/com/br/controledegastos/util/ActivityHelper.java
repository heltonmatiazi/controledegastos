package senac.com.br.controledegastos.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;

/**
 * Created by helton on 09/11/2017.
 */

public class ActivityHelper {
    public static void initialize(Activity activity){
        // previne que o usuario altere a orientação da tela
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
