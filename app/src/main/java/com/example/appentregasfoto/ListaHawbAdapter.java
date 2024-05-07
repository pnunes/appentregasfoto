package com.example.appentregasfoto;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListaHawbAdapter extends BaseAdapter {
    private Context context;
    private List<ModeloHawb> listahawb;

    public ListaHawbAdapter(Context context, List<ModeloHawb> listahawb) {
        this.context = context;
        this.listahawb = listahawb;
    }

    @Override
    public int getCount() { return listahawb.size(); }

    @Override
    public Object getItem(int position) { return listahawb.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context,R.layout.item_lista_hawb,null);

        //Atribui o componente do layout a uma variavel
        TextView tvN_hawb = (TextView)v.findViewById(R.id.tv_nu_hawb);
        TextView tvDestino = (TextView)v.findViewById(R.id.tv_destino);
        TextView tvRua = (TextView)v.findViewById(R.id.tv_rua);
        TextView tvBairro = (TextView)v.findViewById(R.id.tv_bairro);
        TextView tvCidade = (TextView)v.findViewById(R.id.tv_cidade);

        //Carrega a variavel que contem o coponente com o valor da lista
        tvN_hawb.setText(listahawb.get(position).getNhawb());
        tvDestino.setText(listahawb.get(position).getNomeDestino());
        tvRua.setText(listahawb.get(position).getRua());
        tvBairro.setText(listahawb.get(position).getBairro());
        tvCidade.setText(listahawb.get(position).getCidade());
        v.setTag(listahawb.get(position).getId());

        return v;
    }
}
