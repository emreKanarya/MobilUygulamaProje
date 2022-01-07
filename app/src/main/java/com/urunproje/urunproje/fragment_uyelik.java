package com.urunproje.urunproje;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class fragment_uyelik extends Fragment {

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;


    FirebaseStorage storage;
    StorageReference storageReference;
    int sayaccontrol=0;
    Button btngirisbuton;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    EditText editkullaniciadi,password;
    ImageView imgsec;
    static int controluser=0;
    int radioButtonID;
    RadioGroup radio;
    TextView txtgirisbtn;


    public fragment_uyelik() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_fragment_uyelik, container, false);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        txtgirisbtn=view.findViewById(R.id.txtgirisbtn);
        txtgirisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.fragmentsayac=0;
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);


            }
        });

        btngirisbuton=view.findViewById(R.id.btngiris);
        editkullaniciadi=view.findViewById(R.id.editEmail);
        password=view.findViewById(R.id.editSifre);
        imgsec=view.findViewById(R.id.imgsec);
        radio=view.findViewById(R.id.rdGroup);

        imgsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //*******************************
                // popup
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


                //******************************
            }
        });

        try{

            database = FirebaseDatabase.getInstance();
            databaseRef = database.getReference("users");

        }catch (Exception er){
            Toast.makeText(view.getContext(),er.getMessage(),Toast.LENGTH_LONG).show();
        }


        btngirisbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sayaccontrol=0;
                fragment_uyelik.controluser = 0;

                databaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            if (editkullaniciadi.getText().toString().equals(postSnapshot.child("username").getValue().toString())) {


                                fragment_uyelik.controluser = 1;

                                if (sayaccontrol<1){
                                    Toast.makeText(view.getContext(), "Aynı kullanıcı adı sistemde mevcut. Lütfen farklı bir kullanıcı adı giriniz!", Toast.LENGTH_LONG).show();

                                }
                            }
                        }


                        if ( fragment_uyelik.controluser == 0) {

                            if (editkullaniciadi.getText().length()<1 || password.getText().length()<1){
                                Toast.makeText(view.getContext(), "Lütfen alanları boş geçmeyin!", Toast.LENGTH_LONG).show();

                            }else{

                                if(filePath!=null){

                                    //--------------------
                                    uploadImage();
                                    databaseRef.child(editkullaniciadi.getText().toString()).child("username").setValue(editkullaniciadi.getText().toString());
                                    databaseRef.child(editkullaniciadi.getText().toString()).child("password").setValue(password.getText().toString());

                                    radioButtonID = radio.getCheckedRadioButtonId();
                                    if (radioButtonID == R.id.rdkullanici) {
                                        databaseRef.child(editkullaniciadi.getText().toString()).child("kayitturu").setValue(0);
                                    }
                                    if (radioButtonID == R.id.rdmarket) {
                                        databaseRef.child(editkullaniciadi.getText().toString()).child("kayitturu").setValue(1);
                                    }


                                    sayaccontrol++;
                                    //-------------------

                                }else{
                                    Toast.makeText(view.getContext(), "Lütfen profil fotoğrafı seçiniz!", Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(view.getContext(), "Hata!", Toast.LENGTH_SHORT).show();
                    }
                });
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
                        "Profil fotoğrafı seçin..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity(), "Kamera izni verildi.", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(getActivity(), "Kamera izni reddedildi!", Toast.LENGTH_LONG).show();
            }
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
                imgsec.setImageBitmap(bitmap);
                Toast.makeText(getActivity(),"Fotoğraf seçimi başarılı.",Toast.LENGTH_SHORT).show();
            }

            catch (IOException e) {

                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK  && data != null )
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgsec.setImageBitmap(photo);
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
                            "images/"
                                    +editkullaniciadi.getText().toString());

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

}
