package android.example.notes20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.example.notes20.Data.UserNote;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddnoteActivity extends AppCompatActivity {

    TextView txt1;
    TextView txt2;
    Button btn1;
    Toolbar tb;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);

        txt1 =findViewById(R.id.title);
        txt2 = findViewById(R.id.editText3);

        btn1 = findViewById(R.id.btn1);



        sharedPreferences = getSharedPreferences("sharedData", Context.MODE_PRIVATE);

        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(sharedPreferences.getString("uid",""));


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            tb.setTitleTextColor(Color.WHITE);
//            tb.setNavigationIcon(R.drawable.ic_arrow_back);
//            tb.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                Calendar calendar = Calendar.getInstance();
                String date = simpleDateFormat.format(calendar.getTime());
                String title = txt1.getText().toString();
                String desc = txt2.getText().toString();
                if (!title.equalsIgnoreCase(""))
                {
                    if (!desc.equalsIgnoreCase(""))
                    {
                        String key = databaseReference.push().getKey();
                        UserNote userNote = new UserNote(txt1.getText().toString(),txt2.getText().toString(),date,key);
                        databaseReference.child(key).setValue(userNote).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),"Success..",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),""+task.getException().getMessage(),Toast.LENGTH_SHORT);
                                }
                            }
                        });


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Enter Description",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter title",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
