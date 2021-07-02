package com.fesi.funda.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fesi.funda.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentoPaginado extends Fragment {


    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;
    public FragmentoPaginado() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_paginado, container, false);

        if (savedInstanceState == null) {
            insertarTabs(container);

            // Setear adaptador al viewpager.
            viewPager = (ViewPager) view.findViewById(R.id.pager);
            poblarViewPager(viewPager);
            pestanas.setupWithViewPager(viewPager);
        }

        return view;
    }

    private void insertarTabs(ViewGroup container) {
        View padre = (View) container.getParent();
        appBar = (AppBarLayout) padre.findViewById(R.id.appbar);
        pestanas = new TabLayout(getActivity());
        pestanas.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        pestanas.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        appBar.addView(pestanas);
    }

    private void poblarViewPager(ViewPager viewPager) {
        AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
        adapter.addFragment(new CapacitateFesi(), getString(R.string.tab_cursos));
        adapter.addFragment(new TusCursos(), getString(R.string.tus_cursos));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.removeView(pestanas);
    }

    /**
     * Un {@link FragmentStatePagerAdapter} que gestiona las secciones, fragmentos y
     * títulos de las pestañas
     */
    public class AdaptadorSecciones extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentos = new ArrayList<>();
        private final List<String> titulosFragmentos = new ArrayList<>();

        public AdaptadorSecciones(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentos.get(position);
        }

        @Override
        public int getCount() {
            return fragmentos.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentos.add(fragment);
            titulosFragmentos.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titulosFragmentos.get(position);
        }
    }
}