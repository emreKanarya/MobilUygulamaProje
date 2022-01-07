package com.urunproje.urunproje;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class activityUrunTanitim extends AppCompatActivity {

     ImageView imgurun;
    TextView txturunAd,txturunFiyatTarih,txturunFiyat,txturunmarket;
   static String urunAd,FiyatTarih,urunFiyat,urunmarket,imgadres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_tanitim);

        imgurun=findViewById(R.id.imgurunresim);
        txturunAd=findViewById(R.id.txturunAdi);
        txturunmarket=findViewById(R.id.txturunmarketadi);
        txturunFiyatTarih=findViewById(R.id.txturunfiyattarih);
        txturunFiyat=findViewById(R.id.txturunfiyat);

        txturunAd.setText(urunAd);
        txturunFiyat.setText(urunFiyat+" TL");
        txturunFiyatTarih.setText(FiyatTarih);
        txturunmarket.setText(urunmarket);

        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
        storageRef.child("urunler/"+imgadres).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getApplicationContext()).load(uri).into(imgurun);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
