package com.urunproje.urunproje;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentTanitim extends Fragment {

    TextView txtcik;
    ViewPager mViewPager;
    int[] images = {R.drawable.tanitim1, R.drawable.tanitim2, R.drawable.tanitim3, R.drawable.tanitim4,
            R.drawable.tanitim5, R.drawable.tanitim6, R.drawable.tanitim7};
    ViewPagerAdapter mViewPagerAdapter;


    public fragmentTanitim() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_fragment_tanitim, container, false);



        mViewPager = view.findViewById(R.id.viewPager);
        mViewPagerAdapter = new ViewPagerAdapter(getActivity(), images);
        mViewPager.setAdapter(mViewPagerAdapter);

        txtcik=view.findViewById(R.id.txtcikis);
        txtcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentsayac=0;
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

}
