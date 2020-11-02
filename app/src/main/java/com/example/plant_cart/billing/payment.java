package com.example.plant_cart.billing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant_cart.MainActivity;
import com.example.plant_cart.R;
import com.example.plant_cart.categories.cardDetail;
import com.example.plant_cart.testing.UPITesting;
import com.example.plant_cart.testing.cardtesting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class payment extends AppCompatActivity {
    CardView axisbank,cod,UPI;
    ImageView mimage;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    TextView amount;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        axisbank = findViewById(R.id.AxisBank);
        cod = findViewById(R.id.COD);
        UPI = findViewById(R.id.UPI);
        mimage = findViewById(R.id.imageView);
        amount = findViewById(R.id.amount);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        DocumentReference documentReference = firebaseFirestore.collection("bill").document(mAuth.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                String total = snapshot.getString("total"+cardDetail.cnt+"");
                amount.setText("Rs. "+total+"/-");
            }
        });

        UPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Toast.makeText(payment.this,"Payment Through UPI",Toast.LENGTH_SHORT).show();

                Dialog dialog =new Dialog(payment.this);
                dialog.setContentView(R.layout.upi_information_dialog);
                dialog.setCanceledOnTouchOutside(true);
                //set dialog width and height
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                //Set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //set Animation
                dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

                Button proceed = dialog.findViewById(R.id.proceed_upi);
                final EditText upiNumber = dialog.findViewById(R.id.upiNumber);

                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String number = upiNumber.getText().toString().trim();
                        UPITesting testing = new UPITesting();

                        boolean reciver =  testing.validateUPINumber(number);


                        if (reciver == true){

                            Dialog dialog =new Dialog(payment.this);
                            dialog.setContentView(R.layout.success_dialog);
                            dialog.setCanceledOnTouchOutside(false);
                            //set dialog width and height
                            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            //Set transparent background
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            //set Animation
                            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
                            dialog.show();

                            DocumentReference documentReference = firebaseFirestore.collection("users").document(mAuth.getUid());
                            HashMap<String,Object> bill = new HashMap<>();;
                            bill.put("total_bills", cardDetail.cnt+"");
                            documentReference.update(bill);

                            //notification
                            long[] pattern = {400, 200, 400};
                            String message = "Your order has been placed successfully. Enjoy shopping!!";
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(payment.this)
                                    .setSmallIcon(R.drawable.message)
                                    .setContentTitle("Wohoo!! Order Placed ")
                                    .setContentText(message)
                                    .setAutoCancel(true);

                            Intent intent = new Intent(payment.this,order.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            PendingIntent pendingIntent = PendingIntent.getActivity(payment.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(pendingIntent);

                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(0, builder.build());

                            Intent intent1 = new Intent(payment.this, MainActivity.class);
                            finish();
                            startActivity(intent1);


                        }
                        else
                            {
                                Toast.makeText(payment.this,"Something Went wrong try again",Toast.LENGTH_SHORT).show();
                                Dialog dialog =new Dialog(payment.this);
                                dialog.setContentView(R.layout.upi_error_dialog);
                                dialog.setCanceledOnTouchOutside(true);
                                //set dialog width and height
                                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                                //Set transparent background
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                //set Animation
                                dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

                                dialog.show();
                            }
                    }
                });

                dialog.show();

            }
        });

        axisbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(payment.this,"Payment Through Axis Bank card",Toast.LENGTH_SHORT).show();

                Dialog dialog =new Dialog(payment.this);
                dialog.setContentView(R.layout.creditcard_information_dialog);
                dialog.setCanceledOnTouchOutside(true);
                //set dialog width and height
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                //Set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //set Animation
                dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

                Button proceed = dialog.findViewById(R.id.proceed_payment);
                final EditText creditcardnumber = dialog.findViewById(R.id.creditcardNumber);

                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String number = creditcardnumber.getText().toString().trim();
                        cardtesting testing = new cardtesting();

                        boolean reciver =  testing.validateCreditCardNumber(number);


                        if (reciver == true){

                            Dialog dialog =new Dialog(payment.this);
                            dialog.setContentView(R.layout.success_dialog);
                            dialog.setCanceledOnTouchOutside(false);
                            //set dialog width and height
                            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            //Set transparent background
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            //set Animation
                            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
                            dialog.show();

                            DocumentReference documentReference = firebaseFirestore.collection("users").document(mAuth.getUid());
                            HashMap<String,Object> bill = new HashMap<>();;
                            bill.put("total_bills",cardDetail.cnt+"");
                            documentReference.update(bill);

                            //notification
                            long[] pattern = {400, 200, 400};
                            String message = "Your order has been placed successfully. Enjoy shopping!!";
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(payment.this)
                                    .setSmallIcon(R.drawable.message)
                                    .setContentTitle("Wohoo!! Order Placed ")
                                    .setContentText(message)
                                    .setAutoCancel(true);

                            Intent intent = new Intent(payment.this,order.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            PendingIntent pendingIntent = PendingIntent.getActivity(payment.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(pendingIntent);

                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(0, builder.build());

                            Intent intent1 = new Intent(payment.this, MainActivity.class);
                            finish();
                            startActivity(intent1);


                        }
                        else {
                            Toast.makeText(payment.this,"Something Went wrong try again",Toast.LENGTH_SHORT).show();
                            Dialog dialog =new Dialog(payment.this);
                            dialog.setContentView(R.layout.error_creditcard_dialog);
                            dialog.setCanceledOnTouchOutside(true);
                            //set dialog width and height
                            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            //Set transparent background
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            //set Animation
                            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

                            dialog.show();
                        }
                    }
                });

                dialog.show();

            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog =new Dialog(payment.this);
                dialog.setContentView(R.layout.success_dialog);
                dialog.setCanceledOnTouchOutside(false);
                //set dialog width and height
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                //Set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //set Animation
                dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
                dialog.show();

                DocumentReference documentReference = firebaseFirestore.collection("users").document(mAuth.getUid());
                HashMap<String,Object> bill = new HashMap<>();;
                bill.put("total_bills",cardDetail.cnt+"");
                documentReference.update(bill);

                long[] pattern = {400, 200, 400};

                //notification
                String message = "Your order has been placed successfully. Enjoy shopping!!";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(payment.this)
                        .setSmallIcon(R.drawable.message)
                        .setContentTitle("Wohoo!! Order Placed ")
                        .setContentText(message)
                        .setAutoCancel(true);

                Intent intent = new Intent(payment.this,order.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(payment.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, builder.build());

                Intent intent1 = new Intent(payment.this, MainActivity.class);
                finish();
                startActivity(intent1);
            }
        });



    }

}