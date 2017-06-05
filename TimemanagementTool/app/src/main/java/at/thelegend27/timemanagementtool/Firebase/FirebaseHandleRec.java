package at.thelegend27.timemanagementtool.Firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by toedtli2 on 25.05.2017.
 */

public class FirebaseHandleRec {

    FirebaseRecordEntity record;
    String actualKey = "";

    public void addRecord(FirebaseRecordEntity newRecord) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("records");

        ref.push().setValue(newRecord);
    }


    public void updateRecord(final String userID, final String date, final String param, final String value) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("records");
        Query query = ref.orderByChild("userID").equalTo(userID);//.limitToLast(1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot e : dataSnapshot.getChildren()) {
                    FirebaseRecordEntity rec = e.getValue(FirebaseRecordEntity.class);
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference ref;

                    //Log.i("TEST:", "User-ID: " + rec.getUserID() + "Date: " + rec.getDate());

                    if ( rec.getDate().equals(date)) {
                        ref = db.getReference("records/" + e.getKey());
                        ref.child(param).setValue(value);
                        //Log.i("UPDATE: ", ""+value);
                    }

                    else
                        actualKey = "";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}