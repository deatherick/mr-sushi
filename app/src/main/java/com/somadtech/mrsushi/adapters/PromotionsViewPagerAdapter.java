package com.somadtech.mrsushi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.activities.PromotionsDetailActivity;
import com.somadtech.mrsushi.entities.Promotion;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by smt on 2/14/17.
 * Project: mrsushi-android
 */

public class PromotionsViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Promotion> mResources;

    public PromotionsViewPagerAdapter(Context mContext, List<Promotion> mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        Picasso.with(mContext)
                .load(mResources.get(position).getImage_small())
                .placeholder(R.drawable.staticmap)
                .error(R.drawable.staticmap)
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PromotionsDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("promotion_id", mResources.get(position).getId());
                mContext.startActivity(intent);
            }
        });
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
