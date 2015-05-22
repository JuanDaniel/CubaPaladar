package org.cuba.paladar.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import org.cuba.paladar.Model.Entities.Image;
import org.cuba.paladar.R;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    private List<Image> images;
    private List<View> pages;
    private int count;

    public ImagePagerAdapter(Context context, List<Image> images) {
        this.context = context;
        this.images = images;
        this.pages = new ArrayList<View>();
        this.count = images.size();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View page = null;

        if (pages.size() - 1 < position) {
            page = inflater.inflate(R.layout.restaurant_images, null);

            ImageView image = (ImageView) page
                    .findViewById(R.id.imageViewPager);

            if (images != null && count > 0) {
                image.setImageBitmap(images.get(position).getImage());
            } else {
                image.setImageResource(R.drawable.ic_paladar_no_image);
            }
            pages.add(page);

            ImageView arrowPrev = (ImageView) page
                    .findViewById(R.id.imageViewPrev);
            ImageView arrowNext = (ImageView) page
                    .findViewById(R.id.imageViewNext);

            // Arrow logic
            if (position == 0) {
                arrowPrev.setVisibility(View.GONE);
                if (count == 1) {
                    arrowNext.setVisibility(View.GONE);
                }
            }
            if (position == count - 1) {
                arrowNext.setVisibility(View.GONE);
            }
        } else {
            page = pages.get(position);
        }

        ((ViewPager) container).addView(page, 0);

        return page;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

}
