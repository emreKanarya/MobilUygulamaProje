package com.urunproje.urunproje;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_urunler extends Fragment {

    View view;
    FirebaseDatabase firebasedb;
    List<fireModelUrunler> list;
    public static List<String> marketisimleri;
    RecyclerView recurun;
    DatabaseReference yenirefj,yenirefmarket;
    public static String nameMarkets;
    public static int i=0;
    EditText editAranan;
    ImageView imgAra;

    public static String arananKelime="";

    public static DataSnapshot data11esit;
    public fragment_urunler() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_urunler, container, false);

        fragment_urunler.arananKelime="";

        editAranan=view.findViewById(R.id.editurunara);
        imgAra=view.findViewById(R.id.imgurunara);
        recurun=view.findViewById(R.id.recurunler);
        imgAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editAranan.getText().toString().length() > 2 || editAranan.getText().toString().length() == 0){
                    fragment_urunler.arananKelime=editAranan.getText().toString();
                    getUrunler(fragment_urunler.arananKelime);

                }else{

                    Toast.makeText(getActivity(),"En az 3 karakter ile arama yapınız!",Toast.LENGTH_SHORT).show();

                }

            }
        });



        getUrunler(fragment_urunler.arananKelime);


        return view;
    }

int sayac=-1;
    public void getUrunler(final String aranan){

        try{
            sayac=-1;
            recurun.clearFocus();
            firebasedb = FirebaseDatabase.getInstance();
            yenirefj = firebasedb.getReference("users");
            yenirefmarket = firebasedb.getReference("markets");
            fragment_urunler.marketisimleri=new ArrayList<>();
            list = new ArrayList<fireModelUrunler>();

            yenirefj.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sayac=-1;
                    fragment_urunler.marketisimleri.clear();

                    list = new ArrayList<fireModelUrunler>();

                    for(DataSnapshot dataSnapshot11 :dataSnapshot.getChildren()){

                        if (dataSnapshot11.child("kayitturu").getValue().toString().equals("1")){
                            fragment_urunler.nameMarkets=dataSnapshot11.child("username").getValue().toString();

                                fragment_urunler.marketisimleri.add(dataSnapshot11.child("username").getValue().toString());


                                yenirefmarket.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot5) {

                                        sayac++;

                                        if (dataSnapshot5.child(  fragment_urunler.marketisimleri.get(sayac)).exists()==true){

                                            for(DataSnapshot dataSnapshot3 :dataSnapshot5.child( fragment_urunler.marketisimleri.get(sayac)).getChildren()){

                                                fireModelUrunler value = dataSnapshot3.getValue(fireModelUrunler.class);
                                                fireModelUrunler fire = new fireModelUrunler();

                                                System.out.println("indekss : "+value.getUrunAdi().indexOf(aranan)+"");

                                                    if (value.getUrunAdi().indexOf(aranan)!=-1){

                                                        fire.setMarket(value.getMarket());
                                                        fire.setStok(value.getStok());
                                                        fire.setUrunAdi(value.getUrunAdi());
                                                        fire.setUrunFiyat(value.getUrunFiyat());
                                                        fire.setUrunFiyatTarihi(value.getUrunFiyatTarihi());
                                                        fire.setUrunID(value.getUrunID());

                                                        list.add(fire);

                                                    }

                                            }

                                            RecyclerAdapterUrunler recyclerAdapter = new RecyclerAdapterUrunler(list,getActivity());
                                            RecyclerView.LayoutManager recyce = new LinearLayoutManager(getContext());
                                            recurun.setLayoutManager(recyce);
                                            recurun.setItemAnimator( new DefaultItemAnimator());
                                            recurun.setAdapter(recyclerAdapter);

                                            //**************
                                        }
                                        //*****************
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                        } }}

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}
