package com.aparat.money;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aparat.money.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private ImageButton editBTN;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

    user = FirebaseAuth.getInstance().getCurrentUser();
    reference = FirebaseDatabase.getInstance().getReference("Users");
    userID = user.getUid();

    editBTN = (ImageButton) findViewById(R.id.editBalanceBTN);
    editBTN.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this, android.R.style.Holo_ButtonBar_AlertDialog);

            final View customLayout = getLayoutInflater().inflate(R.layout.balance_layout, null);
            final EditText et = (EditText) customLayout.findViewById(R.id.editBalanceET);

            builder.setView(customLayout);

            builder.setTitle("Edit balance");

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String sTextFromET = et.getText().toString();
                    int nIntFromET = new Integer(sTextFromET).intValue();
                    reference.child(userID).child("balance").setValue(nIntFromET);
                    Toast.makeText(ProfileActivity.this, "New balance: " + nIntFromET, Toast.LENGTH_LONG).show();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    });
    final TextView balanceTV = (TextView) findViewById(R.id.balanceTV);

    reference.child(userID).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            User userProfile = snapshot.getValue(User.class);

            if(userProfile != null){
                int balance = userProfile.balance;

                balanceTV.setText("$" + balance);
            }
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {
            Toast.makeText(ProfileActivity.this, "Something wrong happened!",Toast.LENGTH_LONG).show();
        }
    });
    }
}