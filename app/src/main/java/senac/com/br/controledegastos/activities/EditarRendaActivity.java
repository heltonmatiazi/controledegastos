package senac.com.br.controledegastos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.text.DecimalFormat;

import senac.com.br.controledegastos.DAO.MyORMLiteHelper;
import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.util.RetornoDao;

//Created by Carlos Lohmeyer.

public class EditarRendaActivity extends AppCompatActivity {

    private EditText editRenda, editNotificar;
    private TextView tvRendaAtual, tvNotificarAtual;
    private LinearLayout linear_ajuda_notificar;
    private Mes mes;
    private Dao<Mes, Integer> mesDao;
    private RetornoDao retornoDao;
    private Float valor, valorNotificar, renda, notificar;
    private boolean visaoNotificar = true;
    private String rendaFormatada, notificarFormatado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_renda);
        iniciar();
        mes = retornoDao.retornaMesAtual(this);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if(mes.getRenda() == null){
            tvRendaAtual.setText(getString(R.string.cifrao) + " " + "0.00");
        }else {
            renda = mes.getRenda();
            rendaFormatada = decimalFormat.format(renda);
            tvRendaAtual.setText(getString(R.string.cifrao) + " " + rendaFormatada.replace(",", "."));
        }
        if(mes.getNotificar() == null){
            tvNotificarAtual.setText(getString(R.string.cifrao) + " " + "0.00");
        }else {
            notificar = mes.getNotificar();
            notificarFormatado = decimalFormat.format(notificar);
            tvNotificarAtual.setText(getString(R.string.cifrao) + " " + notificarFormatado.replace(",", "."));
        }
    }

    public void iniciar(){
        editRenda = (EditText) findViewById(R.id.editRenda);
        editNotificar = (EditText) findViewById(R.id.editNotificar);
        linear_ajuda_notificar = (LinearLayout) findViewById(R.id.linear_ajuda_notificar);
        tvRendaAtual = (TextView) findViewById(R.id.tvRendaAtual);
        tvNotificarAtual = (TextView) findViewById(R.id.tvNotificarAtual);
        mes = new Mes();
        retornoDao = new RetornoDao();
    }

    public void editarRenda(View view) throws SQLException {
        if(editRenda.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.toast_editar_renda, Toast.LENGTH_LONG).show();
            editRenda.requestFocus();
            return;
        }
        valor = Float.valueOf(editRenda.getText().toString());
        if(editNotificar.getText().toString().isEmpty()){
            valorNotificar = Float.valueOf("0.00");
        }else {
            valorNotificar = Float.valueOf(editNotificar.getText().toString());
        }
        mesDao = MyORMLiteHelper.getInstance(this).getMesDao();
        if(mes.getRenda() == null){
            mes.setRenda(valor);
            mes.setSaldoMensal(valor);
            mes.setNotificar(valorNotificar);
            Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
            if(res.isUpdated()){
                Toast.makeText(this, R.string.toast_editar_renda_ok, Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, R.string.toast_erro, Toast.LENGTH_LONG).show();
            }
        } else {
            mes.setRenda(valor);
            mes.setSaldoMensal(retornoDao.retornaSaldoMensal(this, valor));
            mes.setNotificar(valorNotificar);
            Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
            if(res.isUpdated()){
                Toast.makeText(this, R.string.toast_editar_renda_ok, Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, R.string.toast_erro, Toast.LENGTH_LONG).show();
            }
        }
        editRenda.setText("");
        editNotificar.setText("");
        mes = null;
        voltar(view);
    }

    public void voltar(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void ajudaNotificar(View view){
        if (visaoNotificar == true){
            mostrarNotificar(view);
        } else {
            esconderNotificar(view);
        }
    }

    public void esconderNotificar(View view){
        linear_ajuda_notificar.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        visaoNotificar = true;
    }

    public void mostrarNotificar(View view){
        linear_ajuda_notificar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        visaoNotificar = false;
    }

}