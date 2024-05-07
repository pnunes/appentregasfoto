package com.example.appentregasfoto;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListaTodasHawbAdapter extends BaseAdapter {

    private Context context;
    private List<ModeloHawb> lisTodasHawb;

    public ListaTodasHawbAdapter(Context context, List<ModeloHawb> lisTodasHawb) {
        this.context = context;
        this.lisTodasHawb = lisTodasHawb;
    }

    @Override
    public int getCount() { return lisTodasHawb.size(); }

    @Override
    public Object getItem(int position) { return lisTodasHawb.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View t = View.inflate(context,R.layout.item_lista_todas_hawbs,null);
        //Atribui o componente do layout a uma variavel
        TextView tvN_hawb = (TextView)t.findViewById(R.id.tv_nu_hawb_t);
        TextView tvDestino = (TextView)t.findViewById(R.id.tv_destino_t);
        TextView tvRua = (TextView)t.findViewById(R.id.tv_rua_t);
        TextView tvBairro = (TextView)t.findViewById(R.id.tv_bairro_t);
        TextView tvCidade = (TextView)t.findViewById(R.id.tv_cidade_t);
        TextView tvdtentrega = (TextView)t.findViewById(R.id.tv_dtentrega_t);

        //Carrega a variavel que contem o coponente com o valor da lista
        tvN_hawb.setText(lisTodasHawb.get(position).getNhawb());
        tvDestino.setText(lisTodasHawb.get(position).getNomeDestino());
        tvRua.setText(lisTodasHawb.get(position).getRua());
        tvBairro.setText(lisTodasHawb.get(position).getBairro());
        tvCidade.setText(lisTodasHawb.get(position).getCidade());
        tvdtentrega.setText(lisTodasHawb.get(position).getDtEntrega());
        t.setTag(lisTodasHawb.get(position).getId());

        return t;
    }
}
