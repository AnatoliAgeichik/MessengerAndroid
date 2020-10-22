package com.example.user.mymes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");


    final private static int MAX_LENGHT=150;
    Button send_mes;
    EditText mes_inp;
    RecyclerView rec;

    ArrayList<String> messages=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send_mes = findViewById(R.id.send_mes);
        mes_inp = findViewById(R.id.mes_inp);
        rec=findViewById(R.id.rec);

        rec.setLayoutManager(new LinearLayoutManager(this));

        final DataAdapter dataAdapter = new DataAdapter(this,messages);

        rec.setAdapter(dataAdapter);

        send_mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mes_inp.getText().toString();

                if (msg.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter Text",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(msg.length()>MAX_LENGHT){
                    Toast.makeText(getApplicationContext(),"Many symbol. Max 150",Toast.LENGTH_SHORT).show();

                    return;
                }
                myRef.push().setValue(msg);
                mes_inp.setText("");
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String msq = dataSnapshot.getValue(String.class);
            messages.add(msq);
            dataAdapter.notifyDataSetChanged();
            rec.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
