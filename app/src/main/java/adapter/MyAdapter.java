package adapter;

import android.content.Context;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithdivya.white_knight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    static Context context;
     ArrayList<Modal> array;
    static String key;


    public MyAdapter(Context context, ArrayList<Modal> array) {
        this.context=context;
        this.array=array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate( R.layout.recycle,parent,false );
        return new MyViewHolder( v );

    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Modal m = array.get( position );
        holder.name.setText( "  "+m.getProductName()+"  " );
        String exp = m.getExpiryDate();
        key=m.getKey();

        SimpleDateFormat formDate = new SimpleDateFormat("dd/MM/yyyy");
        String today= formDate.format(new Date());

try {
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    Date d1=format.parse(exp);
    Date d2=format.parse(today);
    long diff = d1.getTime() - d2.getTime();
    long diffDays = diff / (24 * 60 * 60 * 1000);
    String days=String.valueOf( diffDays );
    int inday=Integer.parseInt( days );
        if(inday>0)
    holder.date.setText(days+" day(s)" );
    if(inday<=0)
    {
        holder.date.setText( "Expired" );
        holder.cardView.setCardBackgroundColor( ContextCompat.getColor( context,R.color.expired) ) ;
    }
    else if(inday<=30)
    {
        holder.cardView.setCardBackgroundColor( ContextCompat.getColor( context,R.color.lesstime) ) ;
    }
    else
    {
        holder.cardView.setCardBackgroundColor( ContextCompat.getColor( context,R.color.safe) ) ;
    }


}
catch (Exception e)
{

}




        String im=m.getImage();
        if(im.equals( "Foods" ))
        {
            holder.image.setImageResource(R.drawable.cutlery );
        }
        else if(im.equals( "Medicines" ))
        {
            holder.image.setImageResource(R.drawable.pills );
        }
        else if(im.equals( "Cosmetics" ))
        {
            holder.image.setImageResource(R.drawable.cosmetics );
        }
        else if(im.equals("Others"))
        {
            holder.image.setImageResource(R.drawable.more );
        }

    }



    @Override
    public int getItemCount() {
        return array.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,date;
        ImageView image;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super( itemView );
            name= itemView.findViewById( R.id.product);
            date= itemView.findViewById( R.id.expiry);
            image=itemView.findViewById( R.id.icon );
            cardView=itemView.findViewById( R.id.card);
        }
    }
}
