package senac.com.br.controledegastos.model;

//Created by Aluno on 01/11/2017.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import senac.com.br.controledegastos.R;

public class AdapterSpinner extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Orcamento> orcamentos;
    private Context ctx;

    public AdapterSpinner (Context context, ArrayList<Orcamento> orcamentos){
        //Gastos que irão preencher o spinner
        this.orcamentos = orcamentos;
        //Responsável por pegar o layout
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
        //Resgatar o item do Spinner pela posição
        Orcamento item = orcamentos.get(position);
        //Resgatar o layout a ser preenchido
        view = inflater.inflate(R.layout.spinner_listar, null);
        //Resgatar o TextView para a inserção do contexto
        TextView tvSpinner = (TextView) view.findViewById(R.id.tvSpinner);
        //Mandando os dados para os elementos do ListView
        tvSpinner.setText(item.getNome());
        return view;
    }
}