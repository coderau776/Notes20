package android.example.notes20.Data;

import java.io.Serializable;

public class UserNote implements Serializable
{
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String title="";

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    String desc="";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String date="";
    String noteId="";
    public UserNote()
    {

    }
    public UserNote(String title, String desc, String date, String noteId)
    {
        this.title=title;
        this.desc=desc;
        this.date=date;
        this.noteId=noteId;
    }

}
