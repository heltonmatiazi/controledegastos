package senac.com.br.controledegastos.model;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import senac.com.br.controledegastos.R;

//Created by Carlos Lohmeyer on 28/09/2017.

public class AdapterLvOrcamento extends BaseAdapter {
    private LayoutInflater inflater;
    Context c;
    private ArrayList<Orcamento> orcamentos;

    public AdapterLvOrcamento(Context context, ArrayList<Orcamento> orcamentos){
        //Orcamentos que preencheram o ListView
        this.orcamentos = orcamentos;
        //Responsavel por pegar o Layout do orcamento
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orcamentos.size();
    }

    @Override
    public Object getItem(int position) {
        return orcamentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //Resgatar o item do ListView pelo position
        Orcamento orcamento = orcamentos.get(position);

        view = inflater.inflate(R.layout.orcamento_lv, null);
        c = inflater.getContext();

        //Resgatar o TextView para a insercao do contexto
        TextView tvNomeOrcamento = (TextView) view.findViewById(R.id.tvNomeOrcamento);
        TextView tvValores = (TextView) view.findViewById(R.id.tvValores);

        //Mandando os dados para os elementos do ListView
        if(orcamento.isGastoMultiplo() == false){
            tvNomeOrcamento.setTextColor(Color.BLACK);
            tvNomeOrcamento.setText(orcamento.getNome());
        }else if(orcamento.isGastoMultiplo() == true){
            if(orcamento.getSaldo() > 0){
                tvNomeOrcamento.setTextColor(Color.BLUE);
            }else if(orcamento.getSaldo() == 0){
                tvNomeOrcamento.setTextColor(Color.BLACK);
            }else if(orcamento.getSaldo() < 0){
                tvNomeOrcamento.setTextColor(Color.RED);
            }
            tvNomeOrcamento.setText(orcamento.getNome());
        }
        if(orcamento.isGastoMultiplo() == false){
            tvValores.setTextColor(Color.BLACK);
            tvValores.setText(c.getString(R.string.lv_valor) + " " + c.getString(R.string.cifrao) + " " + orcamento.getValorInicial() + "\n" +
                    c.getString(R.string.to_string_forma) + " " + orcamento.getFormadePagamento());
        }else if(orcamento.isGastoMultiplo() == true){
            if(orcamento.getSaldo() > 0){
                tvValores.setTextColor(Color.BLUE);
            }else if(orcamento.getSaldo() == 0){
                tvValores.setTextColor(Color.BLACK);
            }else if(orcamento.getSaldo() < 0){
                tvValores.setTextColor(Color.RED);
            }
            tvValores.setText(c.getString(R.string.lv_valor) + " " + c.getString(R.string.cifrao) + " " + orcamento.getValorInicial() + "\n" +
                    c.getString(R.string.lv_saldo) + " " + c.getString(R.string.cifrao) + " " + orcamento.getSaldo());
        }
        return view;
    }

}