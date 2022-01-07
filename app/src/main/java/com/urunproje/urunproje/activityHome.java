package com.urunproje.urunproje;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class activityHome extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final fragment_urunler fragment_urunler=new fragment_urunler();

        moveToFragment(fragment_urunler);

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        navigation.getMenu().findItem(R.id.action_favoriler).setVisible(false);


        if (fragment_uyelikgiris.kayitturu.equals("0")){
            navigation.getMenu().findItem(R.id.action_urunkayit).setVisible(false);
        }else{
            navigation.getMenu().findItem(R.id.action_urunkayit).setVisible(true);
        }
    }

    void moveToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frmHome, fragment).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_urunler:
                    final fragment_urunler fragment_urunler=new fragment_urunler();
                    moveToFragment(fragment_urunler);
                    return true;
                case R.id.action_favoriler:
                    final fragment_favoriler fragment_favoriler=new fragment_favoriler();
                    moveToFragment(fragment_favoriler);
                    return true;
                case R.id.action_urunkayit:
                    final fragment_UrunKayit fragment_UrunKayit=new fragment_UrunKayit();
                    moveToFragment(fragment_UrunKayit);
                    return true;
                case R.id.action_uyelikbilgileri:
                    final fragment_hesap fragment_hesap=new fragment_hesap();
                    moveToFragment(fragment_hesap);
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onBackPressed() {
        return;
    }

}


