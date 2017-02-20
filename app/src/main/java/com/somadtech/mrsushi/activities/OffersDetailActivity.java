package com.somadtech.mrsushi.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.entities.Cart;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.entities.Variant;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;
import com.squareup.picasso.Picasso;

public class OffersDetailActivity extends AppCompatActivity {

    MrSushiDbHelper mDbHelper = new MrSushiDbHelper(this);
    TextView txtName;
    TextView txtDesc;
    TextView txtPrice;
    EditText observations_text;
    ImageView imgProduct;
    Product product;
    Variant variant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_detail);
        int  product_id = getIntent().getIntExtra("product_id", 1);

        product = mDbHelper.getProduct(product_id);

        variant = new Variant();

        txtName = (TextView) findViewById(R.id.txtName);
        txtDesc = (TextView) findViewById(R.id.txtDescription);
        imgProduct = (ImageView) findViewById(R.id.product_image);
        txtPrice = (TextView) findViewById(R.id.Price);

        txtName.setText(product.getName());
        txtDesc.setText(product.getDescription());
        txtPrice.setText("Q."+product.getOriginalPrice());
        Picasso.with(this)
                .load(product.getFull_image())
                .placeholder(R.drawable.image1)
                .error(R.drawable.image1)
                .into(imgProduct);

        Button button = (Button) findViewById(R.id.add_to_cart);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                observations_text = (EditText) findViewById(R.id.observations_text);
                String observations = observations_text.getText().toString();
                Cart cart = new Cart(product, variant, observations);
                mDbHelper.createCart(cart);
                finish();
            }
        });
    }
}
