package android.example.notes20;

import android.content.Intent;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.nio.file.FileVisitResult;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity
{
    String ph;
    TextView txt1;
    Button btn1;
    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationStateChangedCallbacks;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        btn1 = findViewById(R.id.button);
        txt1 = findViewById(R.id.phone1);

        firebaseAuth = FirebaseAuth.getInstance();

        verificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithMobile(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        };

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ph = "+91"+txt1.getText().toString();
                if(!ph.equalsIgnoreCase(""))
                {
                    verifyMobile(ph);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter phone number",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void  verifyMobile(String mob)
    {
        Toast.makeText(getApplicationContext(),"Wait for few seconds",Toast.LENGTH_SHORT);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mob,60, TimeUnit.SECONDS,this,verificationStateChangedCallbacks);
    }

    public void signInWithMobile(PhoneAuthCredential phoneAuthCredential)
    {
            firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                        FirebaseUser currentUser = task.getResult().getUser();
                        String uid=currentUser.getUid();
                        Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                        intent.putExtra("Mob",ph);
                        intent.putExtra("uid",uid);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),""+task.getException().getMessage(),Toast.LENGTH_SHORT);
                    }
                }
            });
    }
}
