package android.example.notes20;

import android.app.Dialog;
import android.content.Context;
import android.example.notes20.Data.UserNote;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


//con is nothing but context

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder>
{
    Context con;
    ArrayList<UserNote> a = new ArrayList<>();
    Intermediate intermediate;

    public NotesAdapter(Context con, ArrayList<UserNote> a)
    {
        this.con = con;
        this.a =a;
         intermediate= (Intermediate)con;
    }


    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //put the respective xml card file

        View view = LayoutInflater.from(con).inflate(R.layout.activity_notecard,parent,false);
        NotesHolder notesHolder = new NotesHolder(view);

        return notesHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder holder, int position) {
        // show data in text view
        UserNote userNote = a.get(position);

        holder.txt1.setText(userNote.getTitle());
        holder.txt2.setText(userNote.getDesc());
        holder.txt3.setText(userNote.getDate());
        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNote userNote1 = a.get(position);

                String title = userNote1.getTitle();
                String  desc = userNote1.getDesc();

                showDialog(userNote);

            }
        });

        holder.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNote userNote1 = a.get(position);

                dialogShow(userNote1);

            }
        });


    }

    @Override
    public int getItemCount() {
        // count of adapter
        return a.size();
    }


    public class NotesHolder  extends RecyclerView.ViewHolder
    {
        TextView txt1;
        TextView txt2;
        TextView txt3;
        ImageView img1;
        ImageView img2;



        public NotesHolder(@NonNull View view) {
            super(view);
             txt1 = (TextView) view.findViewById(R.id.ctitle);
             txt2 = (TextView)view.findViewById(R.id.cdesc);
             txt3=(TextView)view.findViewById(R.id.date);
             img1 = (ImageView) view.findViewById(R.id.edit);
             img2 = (ImageView)view.findViewById(R.id.delete);

        }

    }
    public void showDialog(UserNote userNote)
    {



        Dialog dialog =new Dialog(con);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.activity_dialog);
        dialog.show();

        String title = userNote.getTitle();
        String desc = userNote.getDesc();


        TextView txt= dialog.findViewById(R.id.utitle);
        txt.setText(title);

        TextView txt1= dialog.findViewById(R.id.ueditText3);
        txt1.setText(desc);

        Button button = dialog.findViewById(R.id.ubtn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy ");
                Calendar calendar = Calendar.getInstance();
                String update=simpleDateFormat.format(calendar.getTime());

                String noteid = userNote.getNoteId();
                UserNote userNote1 = new UserNote(txt.getText().toString(),txt1.getText().toString(),update,noteid);

                //Can be done like this but its gawar bcoz in this case note adapter can access anything from mainactivity lets go with some proffesionalism
//                MainActivity mainActivity = new MainActivity();
//                mainActivity.updatewrite(userNote1);

                // here notesadappter can only access interface overriden methods


                intermediate.updatewrite(userNote1);
                dialog.dismiss();
            }
        });
    }

    public void dialogShow(UserNote userNote)
    {

        Dialog dialog = new Dialog(con);
        dialog.setContentView(R.layout.activity_confirm);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        TextView txt1=dialog.findViewById(R.id.yes);
        TextView txt2=dialog.findViewById(R.id.no);

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intermediate.udateremove(userNote);
                dialog.dismiss();

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
