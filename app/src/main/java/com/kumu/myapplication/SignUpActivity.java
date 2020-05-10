package com.kumu.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText eMailText,phoneText,passText;
    ImageView imageView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList<String> phoneNumberDataList;
    Control control = new Control();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        eMailText = findViewById(R.id.eMailText);
        phoneText = findViewById(R.id.phoneText);
        passText = findViewById(R.id.passText);
        imageView = findViewById(R.id.imageView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = firebaseFirestore.collection("UserInfos");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(SignUpActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if(queryDocumentSnapshots != null){
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        //Database deki phone değerlerini bir diziye atama işlemlerini burda gerçekleştirdik
                        Map<String, Object> data = documentSnapshot.getData();
                        String temporary = (String) data.get("phone");
                        phoneNumberDataList.add(temporary);
                        temporary = "";
                    }
                }
                System.out.println(phoneNumberDataList);
                control.setPhoneNumberList(phoneNumberDataList);
            }
        });
    }

    public void signUpButton(View view){

        String eMail = eMailText.getText().toString().trim();
        String phone = phoneText.getText().toString().trim();
        String pass = passText.getText().toString().trim();
        control.setPhoneText(phone);
        controlEmptiesAndCreateUser(eMail,phone,pass);
    }

    private void controlEmptiesAndCreateUser(String eMail, String phone, String pass) {
        if(eMail.matches("") | phone.matches("") | pass.matches("")){
            if(control.getPhoneNumberList() == 1){
                Toast.makeText(this, "This phone number already used,Please sign in..", Toast.LENGTH_SHORT).show();
            }
        }else{
            createUser(eMail,phone,pass);
            Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void createUser(final String eMail, final String phone, final String pass) {

        firebaseAuth.createUserWithEmailAndPassword(eMail,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String,Object> data = new HashMap<>();
                data.put("email",eMail);
                data.put("phone",phone);
                data.put("pass",pass);

                firebaseFirestore.collection("UserInfos").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(SignUpActivity.this, "Creating Complete", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}
