package com.example.mspc_event;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotesList extends AppCompatActivity {

    private static final String TAG = NotesList.class.getSimpleName();
    private DatabaseReference dbRef;
    private Context context;
    LinearLayout layout;
    static int total_notes=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(NotesList.this,AddNote.class);
                Log.d(TAG,"User wants to create a new Note.");
                i.putExtra("NotesID",total_notes+1);
                startActivity(i);
            }
        });
        Log.d(TAG,"Crashing in 3,2,1..........!");
        showAllNotes();
    }
    protected void showAllNotes (){

        Log.d(TAG,"Attempting to show all the notes from the cloud");
        dbRef = FirebaseDatabase.getInstance().getReference();
        context = getApplicationContext();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                layout = findViewById(R.id.linearlayout);
                layout.removeAllViews();
                for(int i = 1;i <= Integer.parseInt(dataSnapshot.child("Notes").child("Total").getValue().toString()); i++){
                    total_notes=i;
                    String cnt = i + "";

                    String heading = dataSnapshot.child("Notes").child(cnt).child("Heading").getValue().toString();
                    String data = dataSnapshot.child("Notes").child(cnt).child("Data").getValue().toString();


                    CardView card = new CardView(context);
                    card.setCardBackgroundColor(Color.parseColor("#404040"));
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                            Toolbar.LayoutParams.MATCH_PARENT,
                            Toolbar.LayoutParams.MATCH_PARENT
                    );
                    params.setMargins(10,00,10,20);
                    params.height = 600;
                    card.setLayoutParams(params);
                    card.setRadius(20);
                    card.setId(i);
                    final int x = i;
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(NotesList.this,AddNote.class);
                            intent.putExtra("NotesID",x);
                            startActivity(intent);
                        }
                    });


                    // Initialize a new TextView to put in CardView
                    TextView tv = new TextView(context);
                    params.setMargins(30,20,30,10);
                    tv.setLayoutParams(params);
                    tv.setText(heading);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
                    tv.setTextColor(Color.WHITE);

                    card.addView(tv);
                    params.setMargins(30,100,30,0);
                    TextView tv2 = new TextView(context);
                    tv2.setLayoutParams(params);
                    tv2.setText(data);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                    tv2.setTextColor(Color.WHITE);
                    card.addView(tv2);

                    layout.addView(card);


                    Log.d(TAG,heading);
                    Log.d(TAG,data);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}