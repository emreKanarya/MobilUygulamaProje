package com.urunproje.urunproje;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_hesap extends Fragment {

    View view;
    TextView txtkadi;
    ImageView imgcikis,imgprofil;
    private Session session;
    Button btndegistir,uyesil;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    EditText editsifre;
    Query deletequery;

    public fragment_hesap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_fragment_hesap, container, false);
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("users");
        editsifre=view.findViewById(R.id.edityenisifre);
        imgprofil=view.findViewById(R.id.imgprof);
        uyesil=view.findViewById(R.id.btnuyeliksil);
        uyesil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Hesap Silme");
                builder.setMessage("Üyeliğinizi silmek istediğinize emin misiniz?");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try{

                            deletequery = databaseRef.child(MainActivity.usernamee);
                            deletequery.getRef().removeValue();
                            session = new Session(getActivity());
                            session.setusename("");

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(view.getContext(), "Üyelik Kapandı!", Toast.LENGTH_LONG).show();


                        }catch (Exception er){
                            Toast.makeText(view.getContext(), er.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
                builder.show();

            }
        });

        txtkadi=view.findViewById(R.id.txtkadi);
        txtkadi.setText("Üyelik Adı : "+MainActivity.usernamee);

        //*/********************

        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
        storageRef.child("images/"+MainActivity.usernamee).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(view.getContext()).load(uri).into(imgprofil);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        //*/**********************


        imgcikis=view.findViewById(R.id.imgcikisyap);
        imgcikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Çıkış");
                builder.setMessage("Çıkış yapmayı onaylıyor musunuz?");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        session = new Session(getActivity());
                        session.setusename("");

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

                    }
                });
                builder.show();

            }
        });

        btndegistir=view.findViewById(R.id.btnsifredegistir);
        btndegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editsifre.getText().length()<1){
                    Toast.makeText(view.getContext(), "Lütfen alanları boş geçmeyin!", Toast.LENGTH_LONG).show();

                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Şifre Değişikliği");
                    builder.setMessage("Şifre değişikliğini onaylıyor musunuz?");
                    builder.setNegativeButton("Hayır", null);
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            try{
                                databaseRef.child(MainActivity.usernamee).child("password").setValue(editsifre.getText().toString());
                                Toast.makeText(view.getContext(), "Şifre başarıyla değiştirildi.", Toast.LENGTH_LONG).show();

                            }catch (Exception er){
                                Toast.makeText(view.getContext(), "Hata!", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                    builder.show();

                }
            }
        });

        return view;
    }

}
