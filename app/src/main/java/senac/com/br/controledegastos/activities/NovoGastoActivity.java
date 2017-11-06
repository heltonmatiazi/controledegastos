package senac.com.br.controledegastos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import senac.com.br.controledegastos.R;
import static java.lang.Float.parseFloat;

public class NovoGastoActivity extends AppCompatActivity {
    EditText nomeGasto;
    EditText localGasto;
    TextView valorGasto;
    TextView capturaData;
    Spinner formaPagto;
    Spinner orcamentoSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_gasto);

        nomeGasto = (EditText) findViewById(R.id.nomeGasto);
        localGasto = (EditText) findViewById(R.id.localGasto);
        formaPagto = (Spinner) findViewById(R.id.SpinnerFormaPagamento);
        valorGasto = (TextView) findViewById(R.id.valorGasto);
        capturaData = (TextView) findViewById(R.id.capturaData);

        orcamentoSpinner = (Spinner) findViewById(R.id.orcamentoSpinner);
        //TODO gerar spinner de orcamento
        /*Pegando a lista de objetos do BD e jogando no list e no listview
        listCategorias = categoriaDao.queryForAll();
        adapterCategorias = new ArrayAdapter<Categoria>(this, android.R.layout.simple_spinner_item, listCategorias);
        spCategorias.setAdapter(adapterCategorias);*/
    };
    public void salvarGasto(){
        // santa validação Batman
        String nome = nomeGasto.getText().toString();
        String local = localGasto.getText().toString();
        String forma = formaPagto.getSelectedItem().toString();
        Float valor = parseFloat(valorGasto.getText().toString());
        // fazendo o parse da data
        Calendar c = Calendar.getInstance();
        System.out.println("data atual => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String dataFormamtada = df.format(c.getTime());
        capturaData.setText(dataFormamtada.toString());
        String dataCapturada = capturaData.getText().toString();

        // reunindo todas as strings em uma lista para deixar a validação mais elegante
        List<String> controleDeCampos = new ArrayList<String>();
        controleDeCampos.add(nome);
        controleDeCampos.add(local);
        controleDeCampos.add(forma);
        // checando campos vazios
        for (int i = 0; i < controleDeCampos.size();i++){
            Object item = controleDeCampos.get(i);
            if(item == null || item == " "){
                Toast.makeText(this, "Preencha os campos corretamente", Toast.LENGTH_SHORT).show();
                return;
            }
        };
        // testando o campo 'valor'
        if(valor == null || valor <= 0){
            Toast.makeText(this, "O valor especificado é inválido", Toast.LENGTH_SHORT).show();
            return;
        };
        //TODO validar o spinner de orcamento
    };

    public void voltar(View viw){
        finish();
    }
}