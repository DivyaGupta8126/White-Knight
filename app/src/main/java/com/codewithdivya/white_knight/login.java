package com.codewithdivya.white_knight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import javax.security.auth.login.LoginException;

public class login extends AppCompatActivity {
    TextView under,login,welback,belowback;
    EditText email,password;
    FirebaseAuth auth;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }
        under=findViewById(R.id.textView7);
        email=findViewById( R.id.email );
        password=findViewById( R.id.password );
        auth=FirebaseAuth.getInstance();
        login=findViewById( R.id.textView5 );
        welback=findViewById( R.id.textView1 );
        belowback=findViewById( R.id.textView2 );
        progress=new ProgressDialog( this );
        
        under.setPaintFlags(under.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        under.setOnClickListener( new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent( getApplicationContext(), signup.class );
                                          startActivity( intent );
                                          finish();
                                      }
                                  });


                login.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(email.getText().toString().length()==0)
                        {
                            email.setError( "Enter email!" );
                            return;
                        }

                        if(password.getText().toString().length()==0)
                        {
                            password.setError( "Password!" );
                            return;
                        }

                        progress.setMessage( "Loading..." );
                        progress.show();
                        auth.signInWithEmailAndPassword( email.getText().toString(),password.getText().toString() ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    progress.dismiss();
                                    Toast.makeText( getApplicationContext(), "Successfully logged in.", Toast.LENGTH_SHORT ).show();
                                    Intent intent=new Intent(getApplicationContext(),list.class);
                                    startActivity( intent );
                                    finish();
                                }
                                else
                                    progress.dismiss();
                                    Toast.makeText( login.this, task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                                }
                        } );


                    }
                } );
        }
    }

