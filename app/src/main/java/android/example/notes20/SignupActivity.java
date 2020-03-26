package android.example.notes20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.example.notes20.Data.UserInfo;


public class SignupActivity extends AppCompatActivity {

    TextView txt1;
    TextView txt2;
    TextView txt3;
    Button btn1;
    private FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        txt1 = findViewById(R.id.stext);
        txt2 = findViewById(R.id.smail);
        txt3 = findViewById(R.id.spass);
        btn1 = findViewById(R.id.sbtn);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        sharedPreferences = getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //if user have mob no. he/she wont enter password
        if (getIntent().hasExtra("Mob"))
        {
            txt3.setVisibility(View.GONE);
        }
        else
        {
            txt3.setVisibility(View.VISIBLE);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getApplicationContext());
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txt1.getText().toString();
                String email = txt2.getText().toString();
                if (!getIntent().hasExtra("Mob"))
                {
                    String pass = txt3.getText().toString();
                    if(!name.equalsIgnoreCase(""))
                    {
                        if(!email.equalsIgnoreCase(""))
                        {
                            if (!pass.equalsIgnoreCase(""))
                            {
                                Log.i("one","in on click");
                                register(name,email,pass);
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_SHORT);
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Enter email",Toast.LENGTH_SHORT);
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Enter name",Toast.LENGTH_SHORT);

                }
                else
                {
                    String phone = getIntent().getStringExtra("Mob");
                    String uid = getIntent().getStringExtra("uid");

                    if(!name.equalsIgnoreCase(""))
                    {
                        if(!email.equalsIgnoreCase(""))
                        {
                            if (!phone.equalsIgnoreCase(""))
                            {
                                Log.i("one","in on click");

                                UserInfo userInfo = new UserInfo(name,email,phone);

                                Toast.makeText(getApplicationContext(),"Please Wait",Toast.LENGTH_SHORT);

                                databaseReference.child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(),"Success..",Toast.LENGTH_SHORT);

                                            editor.putString("uid",uid);
                                            editor.commit();

                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),""+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_SHORT);
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Enter email",Toast.LENGTH_SHORT);
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Enter name",Toast.LENGTH_SHORT);

                }


            }
        });
    }

    public void register(String name,String email,String pass)
    {

//        progressDialog.setMessage("Please Wait...");
//        progressDialog.show();
            Toast.makeText(getApplicationContext(),"Wait...",Toast.LENGTH_SHORT).show();
            Log.i("two","in function");

        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>(){

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("two","in on click");
                if(task.isSuccessful())
                {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    String uid = currentUser.getUid();

                    UserInfo userInfo=new UserInfo(name,email,"");

                    databaseReference.child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"stored into database",Toast.LENGTH_SHORT).show();

                                editor.putString("uid",uid);
                                editor.commit();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"not stored into database",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(),"Success!!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Failed...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
