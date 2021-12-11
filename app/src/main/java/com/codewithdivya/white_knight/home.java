package com.codewithdivya.white_knight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {
    FirebaseAuth auth;
    View first,second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        first=findViewById( R.id.head );
        second=findViewById( R.id.saviour);
        auth=FirebaseAuth.getInstance();
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        YoYo.with( Techniques.FadeIn).duration( 500 ).repeat( 2 ).playOn( first );
        YoYo.with( Techniques.Pulse).delay( 1100 ).duration( 1000 ).repeat( 1 ).playOn(second );

        Thread thread=new Thread()
        {
            public void run()
            {
                try {
                    sleep( 2*1000 );
                }
                catch (Exception e)
                {

                }
                if(auth.getCurrentUser()!=null)
                {
                    Intent intent=new Intent(getApplicationContext(),list.class);
                    startActivity( intent );
                    finish();
                }
                if(auth.getCurrentUser()==null) {
                    Intent intent = new Intent( getApplicationContext(), login.class );
                    startActivity( intent );
                    finish();
                }
            }
        };
        thread.start();

    }
}