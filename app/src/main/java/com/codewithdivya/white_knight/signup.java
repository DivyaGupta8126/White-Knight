package com.codewithdivya.white_knight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signup extends AppCompatActivity {
    TextView flip,create;
    EditText name,username,email,password;
    ProgressDialog progress;
    FirebaseAuth auth;
    FirebaseDatabase database;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_signup );
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }
        flip=findViewById( R.id.textView9);
        name=findViewById( R.id.name );
        username=findViewById( R.id.username);
        email=findViewById( R.id.email );
        password=findViewById( R.id.password);
        create=findViewById( R.id.textView7);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progress=new ProgressDialog( this );

        flip.setPaintFlags(flip.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        flip.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),login.class);
                startActivity( intent );
                finish();
            }
        } );

        create.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length()==0)
                {
                    name.setError( "Enter name!!" );
                    return;
                }

                if(username.getText().toString().length()<5)
                {
                    username.setError( "Must contain more than 4 characters!" );
                    return;
                }

                if(email.getText().toString().length()==0)
                {
                    email.setError( "Invalid !!" );
                    return;
                }

                char ch;
                int up=0,digit=0;

                String pass=password.getText().toString();
                int len=pass.length();

                for(int i=0;i<len;i++)
                {
                    ch=pass.charAt( i );
                    if(Character.isUpperCase( ch ))
                    {
                        up++;
                    }
                    if(Character.isDigit( ch ))
                    {
                        digit++;
                    }
                }

                if(pass.length()==0)
                {
                    password.setError( "Password!!" );
                    return;
                }

                if(up==0||digit==0)
                {
                    password.setError( "Must contain Uppercase character and digit" );
                    return;
                }

                progress.setMessage( "Creating account..." );
                progress.show();

                auth.createUserWithEmailAndPassword( email.getText().toString(),password.getText().toString()).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful())
                        {
                            progress.dismiss();
                            Toast.makeText( getApplicationContext(), "Your account has been created!", Toast.LENGTH_SHORT ).show();
                            String id=task.getResult().getUser().getUid();
                            HashMap<String,Object> hash=new HashMap<>();
                            hash.put("Name",name.getText().toString());
                            hash.put("Username",username.getText().toString());
                            hash.put("Email",email.getText().toString());
                            hash.put("Password",password.getText().toString());
                            database.getReference().child( "Users" ).child(id ).setValue( hash );
                            Intent intent=new Intent(getApplicationContext(),list.class);
                            startActivity( intent );
                            finish();
                        }
                        else
                        {
                            progress.dismiss();
                            Toast.makeText( getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                        }

                    }
                } );




            }
        } );

    }
}