package com.fesi.funda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.fesi.funda.R;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<String> lottieView;
    private List<String> txtSlider;

    public SliderAdapter(Context context, List<String> lottieView, List<String> txtSlider) {
        this.context = context;
        this.lottieView = lottieView;
        this.txtSlider = txtSlider;
    }

    @Override
    public int getCount() {
        return txtSlider.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        TextView TextoSlider = (TextView) view.findViewById(R.id.textView);
        LottieAnimationView mAnimationView = (LottieAnimationView) view.findViewById(R.id.animation_view);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        TextoSlider.setText(txtSlider.get(position));
        //linearLayout.setBackgroundColor(color.get(position));#1F515F
        mAnimationView.setAnimation(lottieView.get(position).toString());

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
