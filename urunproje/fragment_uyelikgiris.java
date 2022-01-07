package com.urunproje.urunproje;


import android.content.Intent;
import android.os.Bundle;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_uyelikgiris extends Fragment{


   private Session session;
   static String kayitturu;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    TextView txttanitim;

    EditText editEmail;
    EditText editSifre;
    View view;

    Button btngiris;

    public fragment_uyelikgiris() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_fragment_uyelikgiris, container, false);
        FloatingActionButton fab1 = view.findViewById(R.id.btnuyeekle);


        txttanitim=view.findViewById(R.id.txttanitim);
        txttanitim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.fragmentsayac=5;
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

         editEmail=view.findViewById(R.id.editEmail);
         editSifre=view.findViewById(R.id.editSifre);

         btngiris=view.findViewById(R.id.btngiris);

         btngiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                databaseRef = database.getReference("users");



                if (editEmail.getText().length()<1 || editSifre.getText().length()<1){
                    Toast.makeText(view.getContext(), "Lütfen alanları boş geçmeyin!", Toast.LENGTH_LONG).show();

                }else{

                    try{


                    }catch (Exception er){
                        Toast.makeText(view.getContext(),er.getMessage(),Toast.LENGTH_LONG).show();
                    }



                databaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                           // System.out.println("kadi : "+postSnapshot.child("username").getValue().toString());

                           if (editEmail.getText().toString().equals(postSnapshot.child("username").getValue().toString()) && editSifre.getText().toString().equals(postSnapshot.child("password").getValue().toString()))

                    {
                        Toast.makeText(getActivity(), "Giriş Basarili", Toast.LENGTH_SHORT).show();
                        session = new Session(getActivity());
                        session.setusename(postSnapshot.child("username").getValue().toString());
                        session.setkayitturu(postSnapshot.child("kayitturu").getValue().toString());
                        fragment_uyelikgiris.kayitturu=postSnapshot.child("kayitturu").getValue().toString();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);


                    }else{

                    }}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                    //****************************//

                }


            }
        });


fab1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        MainActivity.fragmentsayac=1;
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
});






        return view;


    }



}
