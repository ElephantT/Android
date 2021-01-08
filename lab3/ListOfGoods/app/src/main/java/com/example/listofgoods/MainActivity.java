package com.example.listofgoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Product> products = new ArrayList();
    private DataAdapter productAdapter;
    private static final int REQUEST_CHANGE_TYPE = 1;
    private static final int REQUEST_CREATE_TYPE = 2;
    private Product selectedProduct;

    ListView productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialData();
        productList = (ListView) findViewById(R.id.albumList);
        productAdapter = new DataAdapter(this, R.layout.list_item, products);
        productList.setAdapter(productAdapter);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                selectedProduct = (Product) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                intent.putExtra("create", false);
                intent.putExtra("name", selectedProduct.getName());
                intent.putExtra("store", selectedProduct.getStore());
                intent.putExtra("image", selectedProduct.getImage());
                intent.putExtra("date", selectedProduct.getDate());
                startActivityForResult(intent, REQUEST_CHANGE_TYPE);
            }
        };
        productList.setOnItemClickListener(itemListener);
    }

    public void onAddButtonClick(View v) {
        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
        intent.putExtra("create", true);
        startActivityForResult(intent, REQUEST_CREATE_TYPE);
    }

    private void setInitialData(){
        products.add(new Product("Apple", "Евроопт", R.drawable.p1, "2020"));
        products.add(new Product("Cucumber", "Евроопт", R.drawable.p2, "2020"));
        products.add(new Product("Orange", "Евроопт", R.drawable.p3, "2021"));
        products.add(new Product("Banana", "Корона", R.drawable.p4, "2020"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CHANGE_TYPE) {
            if(resultCode == RESULT_OK) {
                if (data.getExtras().getBoolean("delete"))
                {
                    productAdapter.remove(selectedProduct);
                    productAdapter.notifyDataSetChanged();
                    return;
                }
                selectedProduct.setName(data.getExtras().getString("name"));
                selectedProduct.setStore(data.getExtras().getString("store"));
                selectedProduct.setDate(data.getExtras().getString("date"));
                selectedProduct.setImage(data.getExtras().getInt("image"));
                productAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == REQUEST_CREATE_TYPE) {
            if (resultCode == RESULT_OK) {
                Product newProduct = new Product(data.getExtras().getString("name"),
                        data.getExtras().getString("store"),
                        data.getExtras().getInt("image"), data.getExtras().getString("date"));
                productAdapter.add(newProduct);
                productAdapter.notifyDataSetChanged();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
