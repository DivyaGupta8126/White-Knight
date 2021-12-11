package com.codewithdivya.white_knight;


//import static adapter.MyAdapter.delete;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.VoiceInteractor;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRouter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import adapter.Modal;
import adapter.MyAdapter;

public class list extends AppCompatActivity {
    Button add, pencil, scan;
    TextView result;
    Button log;
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView recyclerView;
    ArrayList<Modal> array;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list );
        pencil = findViewById( R.id.pencil );
        scan = findViewById( R.id.scan );
        add = findViewById( R.id.plus );
        recyclerView = findViewById( R.id.recycler );
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        array = new ArrayList<>();
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        adapter = new MyAdapter( list.this, array );
        recyclerView.setAdapter( adapter );


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle( "Products" );
        }


        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pencil.getVisibility() == View.GONE) {
                    pencil.setVisibility( View.VISIBLE );
                    scan.setVisibility( View.VISIBLE );

                } else {
                    pencil.setVisibility( View.GONE );
                    scan.setVisibility( View.GONE );
                }
            }
        } );

        pencil.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getApplicationContext(), details.class );
                startActivity( intent );
            }
        } );

        scan.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator( list.this );
                intentIntegrator.setPrompt( "Scan your barcode!" );
                intentIntegrator.setBeepEnabled( true );
                intentIntegrator.setCaptureActivity( scan.class );
                intentIntegrator.initiateScan();
            }
        } );


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child( "Users" ).child( auth.getUid() ).child( "Products" );
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                array.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String key = snap.getKey();
                    DatabaseReference newref = ref.child( key );
                    newref.addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Modal m = snap.getValue( Modal.class );
                            array.add( m );
                            adapter.notifyDataSetChanged();
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    } );


                }
                adapter.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        } );




        ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position;
                Modal deleted;
                position=viewHolder.getAdapterPosition();

                deleted=array.get( position );
                FirebaseDatabase.getInstance().getReference().child( "Users" ).child( auth.getUid() ).child( "Products" ).child( deleted.getKey() ).removeValue();

                Snackbar.make( recyclerView,deleted.getProductName(), BaseTransientBottomBar.LENGTH_LONG).setAction( "Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                       HashMap<String,Object> hash=new HashMap<>();
                       hash.put( "ProductName",deleted.getProductName() );
                       hash.put( "ExpiryDate",deleted.getExpiryDate() );
                       hash.put( "Image",deleted.getImage() );
                       hash.put( "Key",deleted.getKey() );
                       FirebaseDatabase.getInstance().getReference().child( "Users" ).child( auth.getUid() ).child( "Products" ).child( deleted.getKey() ).setValue( hash);
                    }
                } ).setDuration( 7000 ).show();


            }

        };
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper( simpleCallback );
        itemTouchHelper.attachToRecyclerView( recyclerView );

    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        IntentResult intentResult = IntentIntegrator.parseActivityResult( requestCode, resultCode, data );
        if (intentResult.getContents() != null) {
            String result = intentResult.getContents();
            Toast.makeText( getApplicationContext(), result, Toast.LENGTH_SHORT ).show();

            RequestQueue requestQueue= Volley.newRequestQueue( this );
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest( Request.Method.GET, "https://www.google.com/search?q="+result, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d( "Message",response.getString( "Name" ) );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                },new Response.ErrorListener()

                {
                    public void onErrorResponse (VolleyError error)
                    {
                            Log.d( "Error", String.valueOf( error ) );
                    }


            } );

            requestQueue.add( jsonObjectRequest );

        } else {
            Toast.makeText( getApplicationContext(), "Unable to read the barcode!!", Toast.LENGTH_SHORT ).show();
        }


    }


}
