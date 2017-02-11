package com.somadtech.mrsushi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    MrSushiDbHelper mDbHelper = new MrSushiDbHelper(this);
    TextView txtName;
    TextView txtDesc;
    ImageView imgProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_product_detail);
        int  product_id = getIntent().getIntExtra("product_id", 1);

        Product product = mDbHelper.getProduct(product_id);

        txtName = (TextView) findViewById(R.id.txtName);
        txtDesc = (TextView) findViewById(R.id.txtDescription);
        imgProduct = (ImageView) findViewById(R.id.product_image);

        txtName.setText(product.getName());
        txtDesc.setText(product.getDescription());
        Picasso.with(this)
                .load(product.getThumbnail())
                .placeholder(R.drawable.image1)
                .error(R.drawable.image1)
                .into(imgProduct);

    }
}
