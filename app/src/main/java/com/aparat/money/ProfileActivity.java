package com.aparat.money;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aparat.money.Model.Cost;
import com.aparat.money.Model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private ImageButton editBTN;
    private Button addBtn;
    private String userID;
    private ListView costLV;
    private int balance;
    private FirebaseListAdapter firebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

    user = FirebaseAuth.getInstance().getCurrentUser();
    reference = FirebaseDatabase.getInstance().getReference("Users");
    userID = user.getUid();
    costLV = (ListView) findViewById(R.id.costLV);

        Query costList = reference.child(userID).child("costs");

    FirebaseListOptions<Cost> costs = new FirebaseListOptions.Builder<Cost>()
            .setLayout(R.layout.cost_item)
            .setLifecycleOwner(ProfileActivity.this)
            .setQuery(costList, Cost.class)
            .build();

    firebaseListAdapter = new FirebaseListAdapter(costs) {
        @Override
        protected void populateView(@NonNull @NotNull View v, @NonNull @NotNull Object model, int position) {
            TextView name = v.findViewById(R.id.costName);
            TextView price = v.findViewById(R.id.costPrice);

            Cost cost = (Cost) model;

            name.setText(((Cost) model).getName().toString());
            price.setText("-" + ((Cost) model).getPrice());
        }
    };

    costLV.setAdapter(firebaseListAdapter);


    editBTN = (ImageButton) findViewById(R.id.editBalanceBTN);
    addBtn = (Button) findViewById(R.id.addBTN);

    addBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this, android.R.style.Holo_ButtonBar_AlertDialog);

            final View customLayout = getLayoutInflater().inflate(R.layout.new_cost, null);
            final EditText nameET = (EditText) customLayout.findViewById(R.id.editName);
            final EditText priceET = (EditText) customLayout.findViewById(R.id.editPrice);

            builder.setView(customLayout);

            builder.setTitle("Add cost");

            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    Cost cost = new Cost();

                    cost.setName(nameET.getText().toString());
                    int nIntFromET = Integer.parseInt(String.valueOf(priceET.getText()));
                    cost.setPrice(nIntFromET);

                    int newBalance = balance - nIntFromET;

                    reference.child(userID).child("balance").setValue(newBalance);

                    reference.child(userID).child("costs").child(String.valueOf(costLV.getCount())).setValue(cost);
                    reference.child(userID).child("costs").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            Toast.makeText(getApplicationContext(), "Succsessful added!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();

                        }
                    });

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
                balance = userProfile.balance;

                balanceTV.setText("$" + balance);
            }
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {
            Toast.makeText(ProfileActivity.this, "Something wrong happened!",Toast.LENGTH_LONG).show();
        }
    });
    }
    @Override
    protected void onStart(){
        super.onStart();
        firebaseListAdapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        firebaseListAdapter.stopListening();
    }
}