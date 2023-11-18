package com.example.insulife.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.insulife.R;
import com.example.insulife.databinding.IntroItemBinding;


public class IntroPagerAdapter extends PagerAdapter {
    Context context;
    int[] images = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4};
    int[] Titles = {R.string.title1, R.string.title2, R.string.title3, R.string.title4};

    int[] Description = {R.string.description1, R.string.description2, R.string.description3, R.string.description4};

    @Override
    public int getCount() {
        return Titles.length;
    }

    public IntroPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.intro_item, container, false);
        IntroItemBinding itemBinding = IntroItemBinding.bind(view);

        itemBinding.introDescription.setText(Description[position]);
        itemBinding.introImage.setImageResource(images[position]);
        itemBinding.introTitle.setText(Titles[position]);

        container.addView(itemBinding.getRoot());

        return itemBinding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
