package senac.com.br.controledegastos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.util.ActivityHelper;

public class ListaDeItensActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this);
        setContentView(R.layout.activity_lista_de_itens);
    }
}
