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
import com.somadtech.mrsushi.entities.Promotion;
import com.somadtech.mrsushi.entities.Variant;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;
import com.squareup.picasso.Picasso;

public class PromotionsDetailActivity extends AppCompatActivity {

    MrSushiDbHelper mDbHelper = new MrSushiDbHelper(this);
    TextView txtName;
    TextView txtDesc;
    TextView txtPrice;
    EditText observations_text;
    ImageView imgProduct;
    Product product;
    Promotion promotion;
    Variant variant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_detail);
        int  promotion_id = getIntent().getIntExtra("promotion_id", 1);

        promotion = mDbHelper.getPromotion(promotion_id);
        product = promotion.getTarget().get(0);

        variant = new Variant();

        txtName = (TextView) findViewById(R.id.txtName);
        txtDesc = (TextView) findViewById(R.id.txtDescription);
        imgProduct = (ImageView) findViewById(R.id.product_image);
        txtPrice = (TextView) findViewById(R.id.Price);

        txtName.setText(product.getName());
        txtDesc.setText(product.getDescription());
        txtPrice.setText("Q." + product.getOriginalPrice());
        Picasso.with(this)
                .load(product.getFull_image())
                .placeholder(R.drawable.image1)
                .error(R.drawable.image1)
                .into(imgProduct);

        Button button = (Button) findViewById(R.id.add_to_cart);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cart cart = new Cart(product, variant, "");
                cart.setPromotion_product(1);
                mDbHelper.createCart(cart);
                finish();
            }
        });
    }
}
