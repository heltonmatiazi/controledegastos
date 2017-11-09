package senac.com.br.controledegastos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;
import senac.com.br.controledegastos.DAO.MyORMLiteHelper;
import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;

//Created by Carlos Lohmeyer.

public class NovoItemActivity extends AppCompatActivity {

    private Orcamento orcamento;
    private EditText editNomeItem, editValorItem;
    private LinearLayout linear_ajuda;
    private boolean visao = true;
    private Dao<Mes, Integer> mesDao;
    private ArrayList<Mes> listMeses;
    private Dao<Orcamento, Integer> orcamentoDao;
    private Mes mes;
    private int idMes;
    private float valor;
    private CheckBox cbMultiplas;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_item);

        resgatarViews();

        try {
            mesDao = MyORMLiteHelper.getInstance(this).getMesDao();
            //Pegando a lista de meses do BD e jogando no list
            listMeses = (ArrayList<Mes>) mesDao.queryForAll();
            //Pegar o mÃªs corrente
            for(int x = 0; x < listMeses.size(); x++){
                if(mes.isMesAtual() == true){
                    mes = listMeses.get(x);
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


        try {
            orcamentoDao = MyORMLiteHelper.getInstance(this).getOrcamentoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resgatarViews(){
        linear_ajuda = (LinearLayout)findViewById(R.id.linear_ajuda);
        editNomeItem = (EditText)findViewById(R.id.nomeItem);
        editValorItem = (EditText)findViewById(R.id.valorItem);
        cbMultiplas = (CheckBox)findViewById(R.id.cbMultiplas);
        orcamento = new Orcamento();
        mes = new Mes();
        listMeses = new ArrayList<>();
    }

    public void mostrar_esconder(View view){
        if (visao == true){
            mostrar(view);
        } else {
            esconder(view);
        }
    }

    public void esconder(View view){
        linear_ajuda.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        visao = true;
    }

    public void mostrar(View view){
        linear_ajuda.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        visao = false;

    }

    public void voltar(View view){
        finish();
    }

    public void salvarItem(View view) throws java.sql.SQLException {
        if(editNomeItem.getText().toString().isEmpty()){
            Toast.makeText(NovoItemActivity.this, R.string.toast_nomeItem, Toast.LENGTH_SHORT).show();
            editNomeItem.requestFocus();
            return;
        }
        value = editValorItem.getText().toString();
        if(cbMultiplas.isChecked()){
            orcamento.setGastoMultiplo(true);
            if(value.isEmpty()){
                value = "0.00";
            }
        }else if(!cbMultiplas.isChecked()){
            orcamento.setGastoMultiplo(false);
            if(value.isEmpty()){
                Toast.makeText(NovoItemActivity.this, R.string.toast_valorItem, Toast.LENGTH_SHORT).show();
                editValorItem.requestFocus();
                return;
            }
        }
        valor = Float.valueOf(value);
        orcamento.setNome(editNomeItem.getText().toString().toUpperCase());
        orcamento.setSaldo(valor);
        orcamento.setValorInicial(valor);
        orcamento.setMes(mes);
        Dao.CreateOrUpdateStatus res = orcamentoDao.createOrUpdate(orcamento);
        if(res.isCreated()){
            Toast.makeText(NovoItemActivity.this, R.string.toast_salvo, Toast.LENGTH_SHORT).show();
        }else if(res.isUpdated()){
            Toast.makeText(NovoItemActivity.this, R.string.toast_atualizado, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(NovoItemActivity.this, R.string.toast_erro, Toast.LENGTH_SHORT).show();
        }
    }

}