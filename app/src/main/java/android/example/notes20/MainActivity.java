package android.example.notes20;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.example.notes20.Data.UserNote;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Intermediate{
    ArrayList <UserNote> notes = new ArrayList<>();

    TextView textView;
    FloatingActionButton btn;
    FloatingActionButton btn2;
    NotesAdapter notesAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;  //for displaying the list of cards vertically we use this manager
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.lets);
        recyclerView = findViewById(R.id.recycle);
        btn =findViewById(R.id.btn);
        btn2 = findViewById(R.id.floatingActionButton2);
        progressBar = findViewById(R.id.bar);

        sharedPreferences =getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(sharedPreferences.getString("uid",""));

        editor = sharedPreferences.edit();

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showD();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddnoteActivity.class);
                startActivity(intent);

            }
        });

        linearLayoutManager =new LinearLayoutManager(this); //layout init
        recyclerView.setLayoutManager(linearLayoutManager); // set layout manager for our view


    }

    @Override
    protected void onResume() {
        super.onResume();
        readNotes();
    }

    public void readNotes()
    {

        notes.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {

                    UserNote userNote = snapshot.getValue(UserNote.class);
                    notes.add(userNote);
                }
                notesAdapter = new NotesAdapter(MainActivity.this,notes);
                recyclerView.setAdapter(notesAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        if(notes.size()==0)
//        {
//            textView.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            textView.setVisibility(View.INVISIBLE);
//        }


    }

    @Override
    public void updatewrite(UserNote userNote) {
        Toast.makeText(getApplicationContext(),"Wait...",Toast.LENGTH_SHORT).show();
        databaseReference.child(userNote.getNoteId()).setValue(userNote).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Success..",Toast.LENGTH_SHORT).show();
                    readNotes();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),""+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        //bhot hard

    }

    @Override
    public void udateremove(UserNote userNote) {
        Toast.makeText(getApplicationContext(),"Wait...",Toast.LENGTH_SHORT).show();
        databaseReference.child(userNote.getNoteId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Success..",Toast.LENGTH_SHORT).show();
                    readNotes();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),""+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showD()
    {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_newacc);
        dialog.show();

        TextView txt1 = dialog.findViewById(R.id.ayes);
        TextView txt2 = dialog.findViewById(R.id.ano);

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                dialog.dismiss();
                editor.putBoolean("status",false);
                editor.commit();
                startActivity(i);
                finish();
            }

        });
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}

