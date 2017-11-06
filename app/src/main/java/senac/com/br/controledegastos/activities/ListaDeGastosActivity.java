package senac.com.br.controledegastos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.AdapterLvGastos;
import senac.com.br.controledegastos.model.AdapterSpinner;
import senac.com.br.controledegastos.model.Gasto;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;
import senac.com.br.controledegastos.util.RetornoDao;

public class ListaDeGastosActivity extends AppCompatActivity {

    private ListView lvGastos;
    private AdapterLvGastos adapterLvGastos;
    private ArrayList<Gasto> gastos, gastosOrcamento;
    private ArrayList<Orcamento> orcamentos;
    private Mes mes;
    private RetornoDao retornoDao;
    private Spinner spinnerListar;
    private AdapterSpinner adapterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_gastos);

        iniciar();
        mes = retornoDao.mesAtual(this);
        orcamentos = retornoDao.listarOrcamentosComGastos(this, mes);
        adapterSpinner = new AdapterSpinner(this, orcamentos);
        spinnerListar.setAdapter(adapterSpinner);
    }

    public void iniciar(){
        gastos = new ArrayList<Gasto>();
        gastosOrcamento = new ArrayList<Gasto>();
        orcamentos = new ArrayList<Orcamento>();
        mes = new Mes();
        spinnerListar = (Spinner) findViewById(R.id.spListar);
        lvGastos = (ListView) findViewById(R.id.lvGastos);
    }

    public void listarGastos(View view){
        //Capturar o item de orcamento selecionado
        Orcamento orcamento = (Orcamento) spinnerListar.getSelectedItem();
        //Com o item, puxar os gastos dele
        gastosOrcamento = retornoDao.listarGastos(this, orcamento);
        //Listar os gastos no listview
        adapterLvGastos = new AdapterLvGastos(this, gastosOrcamento);
        lvGastos.setAdapter(adapterLvGastos);
    }

    public void voltar(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}