package senac.com.br.controledegastos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.util.RetornoDao;

public class EditarRendaActivity extends AppCompatActivity {

    private Mes mes;
    private RetornoDao retornoDao;
    private Float rendaMensal;
    private EditText editRenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_renda);
        resgatarViews();
        mes = retornoDao.mesAtual(this);
        if(mes.getRenda() == null){
            editRenda.setHint(R.string.hint_renda);
        }else {
            rendaMensal = mes.getRenda();
            editRenda.setText(String.valueOf(rendaMensal));
        }
    }

    public void resgatarViews(){
        mes = new Mes();
        editRenda = (EditText) findViewById(R.id.editRenda);
    }

}