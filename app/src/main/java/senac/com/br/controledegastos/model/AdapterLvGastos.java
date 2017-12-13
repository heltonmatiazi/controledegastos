package senac.com.br.controledegastos.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import senac.com.br.controledegastos.R;

//Created by Carlos Lohmeyer on 28/09/2017.

public class AdapterLvGastos extends BaseAdapter {

    private LayoutInflater inflater;
    Context c;
    private ArrayList<Gasto> gastos;

    public AdapterLvGastos(Context context, ArrayList<Gasto> gastos){
        //Gastos que preencheram o ListView
        this.gastos = gastos;
        //Responsavel por pegar o Layout do gasto
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return gastos.size();
    }

    @Override
    public Object getItem(int position) {
        return gastos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //Resgatar o gasto do ListView pelo position
        Gasto gasto = gastos.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        Float valor;
        String valorFormatado;
        valor = gasto.getValor();
        valorFormatado = decimalFormat.format(valor);
        view = inflater.inflate(R.layout.gastos_lv, null);
        c = inflater.getContext();
        //Resgatar o TextView para a insercao do contexto
        TextView tvGastos = (TextView) view.findViewById(R.id.tvGastos);
        //Mandando os dados para os elementos do ListView
        if(gasto.getNome().isEmpty()){
            tvGastos.setText(c.getString(R.string.to_string_local) + " " + gasto.getLocal() + "\n" +
                    c.getString(R.string.to_string_data) + " " + gasto.getDate() + "\n" +
                    c.getString(R.string.to_string_forma) + " " + gasto.getFormadePagamento()  + "\n" +
                    c.getString(R.string.to_string_valor) + " " + valorFormatado.replace(",", "."));
        }else {
            tvGastos.setText(c.getString(R.string.to_string_nome) + " " + gasto.getNome() + "\n" +
                    c.getString(R.string.to_string_local) + " " + gasto.getLocal() + "\n" +
                    c.getString(R.string.to_string_data) + " " + gasto.getDate() + "\n" +
                    c.getString(R.string.to_string_forma) + " " + gasto.getFormadePagamento()  + "\n" +
                    c.getString(R.string.to_string_valor) + " " + valorFormatado.replace(",", "."));
    }
        return view;
    }

}