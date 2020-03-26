package android.example.notes20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    TextView t;
    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txt4;
    Button btn1;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt1 = findViewById(R.id.email);
        txt2 = findViewById(R.id.password);
        txt3 = findViewById(R.id.forgot);
        btn1 = findViewById(R.id.lbtn);
        txt4 = findViewById(R.id.phone);

        //save data
        sharedPreferences = getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firebaseAuth =FirebaseAuth.getInstance();

        t=(TextView) findViewById(R.id.text_signup);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });



        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt1.getText().toString();
                if(!email.equalsIgnoreCase(""))
                {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"password link sent on mail",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"process failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"enter mail",Toast.LENGTH_SHORT).show();
                }
            }
        });


        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OtpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = txt1.getText().toString();
                String password = txt2.getText().toString();

                if(!mail.equalsIgnoreCase(""))
                {
                    if (!password.equalsIgnoreCase(""))
                    {
                        login(mail,password);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"enter email",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(String mail,String password)
    {
        firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    editor.putBoolean("status",true);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"Success!!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    String s = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),""+s,Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}