package com.urunproje.urunproje;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private Session session;
    static String usernamee;
    int sayac=0;
    static int fragmentsayac=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        session = new Session(getApplicationContext());

        if (session.getusename()!=""){
          //  Toast.makeText(getApplicationContext(),session.getusename().toString(),Toast.LENGTH_SHORT).show();
            usernamee=session.getusename();
            fragment_uyelikgiris.kayitturu=session.getkayitturu();
            fragmentsayac=2;
        }else{
            if(fragmentsayac!=5){
                if (fragmentsayac!=1){
                    fragmentsayac=0;
                }
            }else{
                final fragmentTanitim fragmentTanitim=new fragmentTanitim();
                moveToFragment(fragmentTanitim);
            }


        }


        final fragment_uyelik fragment_uyelik=new fragment_uyelik();
        final fragment_uyelikgiris fragment_uyelikgiris=new fragment_uyelikgiris();


        if (fragmentsayac==0){
            moveToFragment(fragment_uyelikgiris);
        }
        if (fragmentsayac==1){
            moveToFragment(fragment_uyelik);
        }
        if (fragmentsayac==2){

            Intent intent = new Intent(getApplicationContext(), activityHome.class);
            startActivity(intent);
        }
    }

    void moveToFragment(Fragment fragment) {
       getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLoayout, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
