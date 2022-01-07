package com.urunproje.urunproje;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_UrunKayit extends Fragment {


    public fragment_UrunKayit() {
        // Required empty public constructor
    }

    View view;
    String urunID;
    private Uri filePath;
    Button btnkayit;
    EditText editurunAdi,editFiyat,editstok;
    ImageView imgurun;
    FirebaseStorage storage;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 22;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    String currentTime;

    private Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_fragment__urun_kayit, container, false);




        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnkayit=view.findViewById(R.id.btnkayityap);
        editurunAdi=view.findViewById(R.id.editurunadi);
        editstok=view.findViewById(R.id.editurunstok);
        editFiyat=view.findViewById(R.id.editurunfiyati);
        imgurun=view.findViewById(R.id.imgproduct);

        imgurun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //*******************************
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("FOTOĞRAF YÜKLEME SEÇENEKLERİ");
                builder.setItems(new CharSequence[] {"Galeri", "Kamera"},
                        new DialogInterface.OnClickListener() {

                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:

                                        SelectImage();

                                        break;

                                    case 1:


                                        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                                        {
                                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                                        }
                                        else
                                        {
                                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                        }

                                        break;

                                    default:
                                        break;
                                }
                            }
                        });

                builder.show();


            }
        });


        btnkayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urunID=UUID.randomUUID().toString();


                if (editurunAdi.getText().length()<1 || editFiyat.getText().length()<1 || editstok.getText().length()<1){
                    Toast.makeText(view.getContext(), "Lütfen alanları boş geçmeyin!", Toast.LENGTH_LONG).show();

                }else{

                    if(filePath!=null){

                        try{

                            database = FirebaseDatabase.getInstance();
                            databaseRef = database.getReference("markets");

                        }catch (Exception er){
                            Toast.makeText(view.getContext(),er.getMessage(),Toast.LENGTH_LONG).show();
                        }



                        currentTime = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


                        uploadImage();
                        databaseRef.child(MainActivity.usernamee).child(urunID).child("market").setValue(MainActivity.usernamee);
                        databaseRef.child(MainActivity.usernamee).child(urunID).child("urunAdi").setValue(editurunAdi.getText().toString());
                        databaseRef.child(MainActivity.usernamee).child(urunID).child("urunFiyat").setValue(editFiyat.getText().toString());
                        databaseRef.child(MainActivity.usernamee).child(urunID).child("stok").setValue(editstok.getText().toString());
                        databaseRef.child(MainActivity.usernamee).child(urunID).child("urunFiyatTarihi").setValue(currentTime.toString());
                        databaseRef.child(MainActivity.usernamee).child(urunID).child("urunID").setValue(urunID);




                    }else{
                        Toast.makeText(view.getContext(), "Lütfen ürün fotoğrafı seçiniz!", Toast.LENGTH_LONG).show();

                    }

                   }

            }
        });



        return view;
    }


    private void SelectImage()
    {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Ürün fotoğrafı seçin..."),
                PICK_IMAGE_REQUEST);
    }



    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            final ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Yükleniyor...");
            progressDialog.show();


            StorageReference ref
                    = storageReference
                    .child(
                            "urunler/"
                                    +urunID);

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getActivity(),
                                                    "Kayıt başarılı",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getActivity(),
                                            "Hata! " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Yükleme "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {


            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getActivity().getContentResolver(),
                                filePath);
                imgurun.setImageBitmap(bitmap);
                Toast.makeText(getActivity(),"Fotoğraf seçimi başarılı.",Toast.LENGTH_SHORT).show();
            }

            catch (IOException e) {

                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK  && data != null )
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgurun.setImageBitmap(photo);
            filePath=getImageUri(photo,Bitmap.CompressFormat.PNG,70);
            Toast.makeText(getActivity(),"Fotoğraf seçimi başarılı.",Toast.LENGTH_SHORT).show();
        }


    }
    public Uri getImageUri(Bitmap src, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);

        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), src, "title", null);
        return Uri.parse(path);
    }
}
