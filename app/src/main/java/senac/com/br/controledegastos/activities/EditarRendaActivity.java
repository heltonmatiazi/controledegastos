package senac.com.br.controledegastos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.util.ActivityHelper;
import senac.com.br.controledegastos.util.RetornoDao;

//Created by Carlos Lohmeyer.

public class EditarRendaActivity extends AppCompatActivity {

    private EditText editRenda;
    private TextView tvRendaAtual;
    Mes mes;
    Dao<Mes, Integer> mesDao;
    RetornoDao retornoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this);
        setContentView(R.layout.activity_editar_renda);
        iniciar();
        mes = retornoDao.retornaMesAtual(this);
        if(mes.getRenda() == null){
            tvRendaAtual.setText("0,00");
        }else {
            tvRendaAtual.setText(String.valueOf(mes.getRenda()));
        }
    }

    public void iniciar(){
        editRenda = (EditText) findViewById(R.id.editRenda);
        tvRendaAtual = (TextView) findViewById(R.id.tvRendaAtual);
        mes = new Mes();
    }

    public void editarRenda(View view) throws SQLException {
        if(editRenda.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.toast_editar_renda, Toast.LENGTH_SHORT).show();
            editRenda.requestFocus();
            return;
        }
        mes.setRenda(Float.valueOf(editRenda.getText().toString()));
        Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
        if(res.isUpdated()){
            Toast.makeText(this, R.string.toast_editar_renda_ok, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, R.string.toast_erro, Toast.LENGTH_SHORT).show();
        }
        editRenda.setText("");
        mes = null;
        voltar(view);
    }

    public void voltar(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}