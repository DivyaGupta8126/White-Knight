package com.codewithdivya.white_knight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class details extends AppCompatActivity {
    EditText date,name;
    ImageView item;
    ImageView foods,medicines,cosmetics,others;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Button add;
    String image="Choose";
    ProgressDialog progress;
    Toolbar tool;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.log)
        {
            auth.signOut();
            Intent intent=new Intent(getApplicationContext(),login.class);
            startActivity( intent );
            finish();
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m=getMenuInflater();
        m.inflate( R.menu.actionbar,menu );
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_details );
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle( "Add Product" );
            int id=getTaskId();
        }

        date=findViewById( R.id.date );
        item=findViewById( R.id.item );
        foods=findViewById( R.id.food );
        cosmetics=findViewById( R.id.cosmetics );
        medicines=findViewById( R.id.medicines );
        others=findViewById( R.id.others );
        name=findViewById( R.id.name );
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        add=findViewById( R.id.add );
        progress=new ProgressDialog(this  );


        date.setShowSoftInputOnFocus( false );
        date.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.setError( null );
                Calendar calendar=Calendar.getInstance();
                int years=calendar.get( Calendar.YEAR );
                int months=calendar.get( Calendar.MONTH );
                int days=calendar.get( Calendar.DAY_OF_MONTH );


                DatePickerDialog datePickerDialog = new DatePickerDialog( details.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String d = String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1)
                                + "/" + String.valueOf(year);
                        date.setText(d);
                    }
                }, years, months, days);
                datePickerDialog.show();



            }
        } );


        item.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(foods.getVisibility()==View.GONE)
                {
                    foods.setVisibility( View.VISIBLE );
                    cosmetics.setVisibility( View.VISIBLE );
                    medicines.setVisibility( View.VISIBLE );
                    others.setVisibility( View.VISIBLE );

                        foods.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                item.setImageResource(R.drawable.cutlery);
                                image="Foods";
                                gone();

                            }
                        } );

                        medicines.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                item.setImageResource( R.drawable.pills );
                                gone();
                                image="Medicines";
                            }
                        } );

                        cosmetics.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                item.setImageResource( R.drawable.cosmetics );
                                gone();
                                image="Cosmetics";
                            }
                        } );

                        others.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                item.setImageResource( R.drawable.more );
                                gone();
                                image="Others";
                            }
                        } );


                }

                else
                {
                    gone();
                }

            }
        } );




        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pName=name.getText().toString();
                String pDate=date.getText().toString();


                if(pName.length()==0) {
                    name.setError( "Enter product name" );
                    return;
                }
                if(pName.length()>20)
                {
                    name.setError( "More than 20 characters!!" );
                    return;
                }
                if(pDate.length()==0)
                {
                    date.setError( "Enter expiry date" );
                    return;
                }
               /* if(pDate.length()!=0)
                {
                    //date.setError( null );

                }*/


                if(image.equals( "Choose" ))
                {
                    Toast.makeText( getApplicationContext(), "select the image", Toast.LENGTH_SHORT ).show();
                    return;
                }

                String id=auth.getCurrentUser().getUid();
                HashMap<String,Object> hash=new HashMap<>();


                hash.put("ProductName",pName);
                hash.put( "ExpiryDate",pDate );
                if(image.equals( "Foods" ))
                    hash.put( "Image","Foods" );
                else if(image.equals( "Medicines" ))
                    hash.put( "Image","Medicines" );
                    else if(image.equals( "Cosmetics" ))
                        hash.put( "Image","Cosmetics" );
                    else
                        hash.put("Image","Others");

              //  database.getReference().child( "Users" ).child( id ).child( "Products" ).push().updateChildren( hash );
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child( "Users" ).child( id ).child( "Products" );
                String key=ref.push().getKey();
                hash.put( "Key",key );
                ref.child(key).setValue( hash );


                Toast.makeText( getApplicationContext(), "Product is added successfully", Toast.LENGTH_SHORT ).show();


            }
        } );






    }

  void gone()
  {
      foods.setVisibility( View.GONE );
      medicines.setVisibility( View.GONE );
      cosmetics.setVisibility( View.GONE );
      others.setVisibility( View.GONE );

  }

    public static boolean validateDate( String date )
    {
        return date.matches("\\b[0-9]{2}/[0-9]{2}/[0-9]{4}\\b");
    }

}


