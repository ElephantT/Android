package com.example.listofgoods;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class DataAdapter extends ArrayAdapter<Product> {

    private LayoutInflater inflater;
    private int layout;
    private List<Product> products;

    public DataAdapter(Context context, int resource, List<Product> products) {
        super(context, resource, products);
        this.products = products;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product product = products.get(position);

        viewHolder.imageView.setImageResource(product.getImage());
        viewHolder.nameView.setText(product.getName());
        viewHolder.storeView.setText(product.getStore());
        viewHolder.dateView.setText(product.getDate());

        return convertView;
    }

    private class ViewHolder {
        final ImageView imageView;
        final TextView nameView, storeView, dateView;
        ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.image);
            nameView = (TextView) view.findViewById(R.id.name);
            storeView = (TextView) view.findViewById(R.id.store);
            dateView = (TextView) view.findViewById(R.id.date);
        }
    }
}
