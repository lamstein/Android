package avcaliani.recyclelist;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import avcaliani.reciclelist.R;

public class MainActivity extends AppCompatActivity {

    protected List<OperatingSystem> mOSList;
    protected RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> mAdapter;
    protected RecyclerView mUIList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUIList = (RecyclerView) findViewById(R.id.recicleList);
        mOSList = new ArrayList<OperatingSystem>();

        populateList();
        mAdapter = new MyRecyclerViewAdapter();
        mUIList.setAdapter(mAdapter);
        mUIList.setLayoutManager(new LinearLayoutManager(this));
    } // End-Function

    protected void populateList(){
        String [] os = {"Android", "Blackberry", "Chrome OS", "Debian",
                        "Fedora", "GNU", "iOS", "Linux Mint", "RedHat",
                        "TUX", "Ubuntu", "Windows"};

        int i = 0, j = 0, k = 0;
        for (i = 0; i < os.length; i++){
            mOSList.add(new OperatingSystem(i, os[i], getIconDrawable(i)));
        }

        for (j = 0; j < os.length; j++){
            for (k = 0; k < 20; k++, i++){
                mOSList.add(new OperatingSystem(i, os[j], getIconDrawable(j)));
            }
        }
    } // End-Function

    protected Drawable getIconDrawable(int id){
        switch (id){
            case 0:
                return getResources().getDrawable(R.drawable.android, null);
                
            case 1:
                return getResources().getDrawable(R.drawable.blackberry, null);

            case 2:
                return getResources().getDrawable(R.drawable.chrome, null);

            case 3:
                return getResources().getDrawable(R.drawable.debian, null);

            case 4:
                return getResources().getDrawable(R.drawable.fedora, null);

            case 5:
                return getResources().getDrawable(R.drawable.gnu, null);

            case 6:
                return getResources().getDrawable(R.drawable.ios, null);

            case 7:
                return getResources().getDrawable(R.drawable.mint, null);

            case 8:
                return getResources().getDrawable(R.drawable.redhat, null);

            case 9:
                return getResources().getDrawable(R.drawable.tux, null);

            case 10:
                return getResources().getDrawable(R.drawable.ubuntu, null);

            case 11:
                return getResources().getDrawable(R.drawable.windows, null);

            default:
                return getResources().getDrawable(R.drawable.test_icon, null);

        }
    }

    protected class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>{

        public class MyViewHolder extends RecyclerView.ViewHolder{

            ImageView osIcon;
            TextView osName;
            TextView osId;

            public MyViewHolder(View itemView) {
                super(itemView);
                osIcon = (ImageView) itemView.findViewById(R.id.itemImage);
                osName = (TextView) itemView.findViewById(R.id.osName);
                osId = (TextView) itemView.findViewById(R.id.itemListNumber);
            } // End-Function
        } // End-Class

        /*
        List<OperatingSystem> mOSList;
        public MyRecyclerViewAdapter(List<OperatingSystem> itemList){
            mOSList = itemList;
        } // End-Function
        */

        // Inflate layout
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        } // End-Function

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {

                OperatingSystem os = mOSList.get(position);
                // Setting OS Name
                holder.osName.setText(os.getName());
                // Setting Image Icon
                Drawable image = os.getImage();
                if (image != null)
                    holder.osIcon.setImageDrawable(image); // Default Image
                // Setting OS ID
                holder.osId.setText("P= " + os.getId());

            }catch (Exception e ){
                Log.i("ERRO", e.getMessage());
            }
        } // End-Function

        @Override
        public int getItemCount() {
            return mOSList.size();
        }

        // Insert a new item to the RecyclerView on a predefined position
        public void insert(int position, OperatingSystem data) {
            mOSList.add(position, data);
            notifyItemInserted(position);
        } // End-Function

        // Remove a RecyclerView item containing a specified Data object
        public void remove(OperatingSystem data) {
            int position = mOSList.indexOf(data);
            mOSList.remove(position);
            notifyItemRemoved(position);
        } // End-Function

    } // End-Class

} // End-Class
