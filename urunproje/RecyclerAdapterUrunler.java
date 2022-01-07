package com.urunproje.urunproje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;


public class RecyclerAdapterUrunler extends RecyclerView.Adapter<RecyclerAdapterUrunler.MyHoder>{

    List<fireModelUrunler> list;
    Context context;
    private String mItem;


    public RecyclerAdapterUrunler(List<fireModelUrunler> list, Context context) {

        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerAdapterUrunler.MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.urunlercard,parent,false);

        RecyclerAdapterUrunler.MyHoder myHoder = new RecyclerAdapterUrunler.MyHoder(view);

        return myHoder;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(final RecyclerAdapterUrunler.MyHoder holder, final int position) {

        final fireModelUrunler mylist = list.get(position);

        holder.txtbaslik.setText(mylist.getUrunAdi());
        holder.txtfiyat.setText("Fiyat : "+mylist.getUrunFiyat()+" TL");
        if (mylist.getStok().equals("0")){
            holder.txtstok.setText("Stokta kalmamıştır!");
            holder.txtstok.setTextColor(Color.parseColor("#9B0000"));
        }else{
            holder.txtstok.setText("Stokta "+mylist.getStok()+" adet bulunmaktadır.");
            holder.txtstok.setTextColor(Color.parseColor("#04293A"));
        }

        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
        storageRef.child("urunler/"+mylist.getUrunID()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(context).load(uri).into(holder.imgurun);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        holder.lntik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityUrunTanitim.urunAd=mylist.getUrunAdi();
                activityUrunTanitim.FiyatTarih=mylist.getUrunFiyatTarihi();
                activityUrunTanitim.urunFiyat=mylist.getUrunFiyat();
                activityUrunTanitim.urunmarket=mylist.getMarket();
                activityUrunTanitim.imgadres=mylist.getUrunID();

                Intent intent = new Intent(context, activityUrunTanitim.class);
                context.startActivity(intent);

            }
        });

        holder.imgurun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityUrunTanitim.urunAd=mylist.getUrunAdi();
                activityUrunTanitim.FiyatTarih=mylist.getUrunFiyatTarihi();
                activityUrunTanitim.urunFiyat=mylist.getUrunFiyat();
                activityUrunTanitim.urunmarket=mylist.getMarket();
                activityUrunTanitim.imgadres=mylist.getUrunID();

                Intent intent = new Intent(context, activityUrunTanitim.class);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try{
            if(list.size()==0){

                arr = 0;
            }
            else{
                arr=list.size();
            }

        }catch (Exception e){
        }

        return arr;
    }

    public class MyHoder extends RecyclerView.ViewHolder  {

        ImageView imgurun;
        TextView txtbaslik,txtfiyat,txtstok;
        LinearLayout lntik;

        public MyHoder(View itemView) {
            super(itemView);

            imgurun=itemView.findViewById(R.id.imgurunresim);
            txtfiyat=itemView.findViewById(R.id.txturunfiyat);
            txtbaslik=itemView.findViewById(R.id.txturunbaslik);
            txtstok=itemView.findViewById(R.id.txturunstok);
            lntik=itemView.findViewById(R.id.lntikla);



        }
        public void setItem(String item) {
            mItem = item;
        }


    }


}