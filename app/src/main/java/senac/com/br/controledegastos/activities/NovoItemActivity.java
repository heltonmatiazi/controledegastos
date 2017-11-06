package senac.com.br.controledegastos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;
import senac.com.br.controledegastos.util.RetornoDao;

public class NovoItemActivity extends AppCompatActivity {

    private Orcamento orcamento;
    private EditText editNomeItem, editValorItem;
    private LinearLayout linear_ajuda;
    private boolean visao = true;
    private Dao<Orcamento, Integer> orcamentoDao;
    private Mes mes;
    private int idMes;
    private float valor;
    private CheckBox cbMultipilas;
    private RetornoDao r;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_item);

        resgatarViews();
        //mes = r.mesAtual(this);
    }

    public void resgatarViews(){
        linear_ajuda = (LinearLayout)findViewById(R.id.linear_ajuda);
        editNomeItem = (EditText)findViewById(R.id.nomeItem);
        editValorItem = (EditText)findViewById(R.id.valorItem);
        cbMultipilas = (CheckBox)findViewById(R.id.rbDiversas);
        orcamento = new Orcamento();
        mes = new Mes();
    }

    public void mostrar_esconder_ajuda(View view){
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
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void salvarItem(View view) throws java.sql.SQLException {
        if(editNomeItem.getText().toString().isEmpty()){
            Toast.makeText(NovoItemActivity.this, R.string.toast_nomeItem, Toast.LENGTH_SHORT).show();
            editNomeItem.requestFocus();
            return;
        }
        value = editValorItem.getText().toString();
        if(cbMultipilas.isChecked()){
            orcamento.setGastoMultiplo(true);
            if(value.isEmpty()){
                value = "0.00";
            }
        }else if(!cbMultipilas.isChecked()){
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
        voltar(view);
    }

}