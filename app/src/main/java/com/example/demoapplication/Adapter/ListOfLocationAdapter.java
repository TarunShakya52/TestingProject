package com.example.demoapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapplication.R;
import com.example.demoapplication.api.GetRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ListOfLocationAdapter extends RecyclerView.Adapter<ListOfLocationAdapter.Adapter> {
    public String sourceType=null;
    private Context context;
    private String [] name = { "Warehouse ","Vender" ,"Vending Machine" };
    private String token = "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIyZDcwM2Y2MC03YWZmLTQwYWItOTg0NC02NDJkODQ3NzRkYTciLCJpZCI6ODYsIm1vYmlsZSI6Ijk1NjA4MDUxMTciLCJyb2xlIjoiQURNSU4iLCJwZXJtaXNzaW9ucyI6WyJ2bV9hbGxvd2VkIiwicHJvZHVjdF9hbGxvd2VkIiwib3JkZXJzX2FsbG93ZWQiLCJ1c2VyX2FsbG93ZWQiLCJ2bV9zZXR0aW5ncyIsInZtX3NhbGVzIiwibm90aWZpY2F0aW9uX2VuYWJsZWQiLCJhYnNvbHV0ZV9zYWxlcyIsInZlbmRvcl9hbGxvd2VkIiwid2FsbGV0X2hpc3RvcnkiLCJ2aWV3X2JyYW5kcyIsImNyZWF0ZV9icmFuZCIsInZpZXdfYmFzZSIsInZpZXdfdmFyaWFudHMiLCJtYXBfdmVuZG9yIiwidmlld19wcm9kdWN0cyIsImNyZWF0ZV9wcm9kdWN0cyIsImNyZWF0ZV9iYXNlIiwiY3JlYXRlX3ZhcmlhbnQiLCJjcmVhdGVfdmVuZG9yIiwidmlld192ZW5kb3IiLCJ2aWV3X3VzZXJzIiwibWFwX3VzZXIiLCJhdHRlbmRhbmNlIiwia2l0dGluZ19yZWZpbGxpbmciLCJhdWRpdF9tYWNoaW5lIiwic3RvY2tpbmciLCJzcGFyZV9wYXJ0cyIsImVfZGFhbGNoaW5pIiwic2xvdF91dGlsaXphdGlvbiIsImhlYWx0aF9hbGVydCIsInRyb3VibGVzaG9vdF9ndWlkZSIsInJhaXNlX3RpY2tldCIsInNlbGZfYXNzZXNzbWVudCIsInJlY2NlIiwidm1faW5zdGFsbGF0aW9uIiwidmlld190YWciLCJhY3RpdmF0ZV90YWciLCJtZWFsc19tZW51Iiwidm1fcGFyYW1ldGVycyIsInN1cHBvcnRfbm90aWZpY2F0aW9uIiwic2xvdF9yZXBhaXIiLCJxdWlja191bmJsb2NrIiwidW5ibG9ja19hbGxfc2xvdHMiLCJzbG90X3JlcG9ydCIsInBhcnRuZXJfcmVjaGFyZ2UiLCJvcmRlcl9hcHByb3ZlIiwiYnVsa19wdXJjaGFzZSIsIm1vYmlsaXR5X3JlZmlsbCIsIm1hcF9icF91c2VyIiwidmlld19mcmFuY2hpc2VlIiwiY3JlYXRlX2ZyYW5jaGlzZWUiLCJ2aWV3X2JhbmsiLCJjcmVhdGVfYmFuayIsInZpZXdfcGxhdGZvcm1fY2hhcmdlIiwibWFwX3BsYXRmb3JtX2NoYXJnZSIsImNyZWF0ZV9wbGF0Zm9ybV9jaGFyZ2UiLCJtb2JpbGl0eV9jaGVja2luIiwiY3JlYXRlX2NvdXBvbiIsInZpZXdfY291cG9uIiwiYnV5X2F0X3ZwIiwibWFwX21hY2hpbmVzIiwidmlld19icF91c2VyIiwidm1fY3JlYXRlIiwidm1fdXBkYXRlIiwidm1fdmlldyIsImNvaG9ydF9jcmVhdGUiLCJjb2hvcnRfdmlldyIsImNvaG9ydF9tYXBfdXNlciIsImNvaG9ydF9tYXBfbWFjaGluZSIsImJhbm5lcl9jcmVhdGUiLCJhcHByb3ZlX2F0dGVuZGFuY2UiLCJzbG90X29wZXJhdGlvbiIsInJlZnVuZF9yZXYiXSwiaWF0IjoxNjc2Mjg0MDMzLCJleHAiOjE2NzYzNzA0MzN9.Gs9o6l0QPh8KBGMxL7vB8GXVlwzewtIKMx8drKWoXwuuEzIBwPhiMZUI-JYebo0B";





    public ListOfLocationAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ListOfLocationAdapter.Adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_location, parent, false);
        return new ListOfLocationAdapter.Adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfLocationAdapter.Adapter holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(name[position]);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position)
                {
                    case 0 : createURLWarehouse("warehouse");
                        break;
                    case 1 : createURLManufactures("manufactures");
                        break;
                    case 2 : createURL("vendingmachines");
                        break;

                }
            }
        });
    }

    private void createURLManufactures(String manufactures) {

    }

    private void createURLWarehouse(String warehouse) {
        String url="https://api-stage.daalchini.co.in/dashboard/api/v5/users/211/warehouses";
        apiforCheckInLOcation(url,warehouse);
    }

    private void  createURL(String data)
    {
        sourceType=data;
        String url= "https://api-stage.daalchini.co.in/dashboard/api/v2/admin/"+sourceType+"/all";
        apiforCheckInLOcation(url,sourceType);
    }
    private void apiforCheckInLOcation(String url, String sourceType) {
        Log.d("apiforInsertion url", "=" + url);
        GetRequest aTask = new GetRequest(context);
        aTask.setListener(new GetRequest.MyAsyncTaskkGetListener() {
            @Override
            public void onPreExecuteConcluded() {
            }

            @Override
            public void onPostExecuteConcluded(String result) {
                Log.d("vend order Response", "=" + result);
                if (result != null) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(result);
                        String Status = jsonObject1.getString("status");
                        String Message = jsonObject1.getString("message");

                        if (Status.toLowerCase().toString().equals("success")) {
                            callFragment(sourceType, result);
                        } else {
                            Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Some Error Occur", Toast.LENGTH_SHORT).show();
                }
            }
        });

        aTask.execute(url, "", "",token);

    }
    public void callFragment(String sourceType,String result)
    {
//        Bundle bundle=new Bundle();
//        bundle.putString("sourceType",sourceType);
//        bundle.putString("response",result);
//        ListOfCheckInLocationFragment listOfCheckInLocationFragment=new ListOfCheckInLocationFragment();
//
//        FragmentManager fragmentManager = ((AttandenceBaseActivity) context).getSupportFragmentManager();
//        if (!fragmentManager.isDestroyed()){
//            listOfCheckInLocationFragment.setArguments(bundle);
//            fragmentManager.beginTransaction().replace(R.id.contentfordata,listOfCheckInLocationFragment).commit();
//        }

    }

    @Override
    public int getItemCount() {
        return name.length;    }

    public class Adapter extends RecyclerView.ViewHolder {
        TextView name;
        public Adapter(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
        }
    }
}
