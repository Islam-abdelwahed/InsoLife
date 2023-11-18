package com.example.insulife.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.insulife.Adapters.IntroPagerAdapter;
import com.example.insulife.R;
import com.example.insulife.databinding.ActivityIntroBinding;


public class IntroActivity extends AppCompatActivity {
    ActivityIntroBinding binding;
    IntroPagerAdapter adapter;
    TextView[] dots;
    private int pos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityIntroBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        adapter=new IntroPagerAdapter(this);
        Intent i=new Intent(this, Login_activity.class);
        binding.introPager.setAdapter(adapter);
        binding.nextButton.setOnClickListener(l->{
            binding.introPager.setCurrentItem(pos+1);
        });
        binding.backButton.setOnClickListener(l->{
            binding.introPager.setCurrentItem(pos-1);
        });
        binding.skipButton.setOnClickListener(l->{
            startActivity(i);
            finish();
        });
        binding.startBtn.setOnClickListener(l->{
            startActivity(i);
            finish();
        });
        addDots(0);
        binding.introPager.addOnPageChangeListener(listener);
    }
    private  void addDots(int postion){
        dots=new TextView[4];
        binding.dotsLayout.removeAllViews();
        for (int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);

            binding.dotsLayout.addView(dots[i]);
        }
        if(dots.length>0){
            dots[postion].setTextColor(getResources().getColor(R.color.main,null));
        }
    }

    ViewPager.OnPageChangeListener listener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            pos=position;
            if(position==3){
                binding.startBtn.setVisibility(View.VISIBLE);
                binding.skipButton.setVisibility(View.INVISIBLE);
                binding.nextButton.setVisibility(View.INVISIBLE);
            }else {
                binding.nextButton.setVisibility(View.VISIBLE);
                binding.skipButton.setVisibility(View.VISIBLE);
                binding.startBtn.setVisibility(View.INVISIBLE);
                binding.backButton.setVisibility(View.VISIBLE);
            }

            if (position==0){
                binding.backButton.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}