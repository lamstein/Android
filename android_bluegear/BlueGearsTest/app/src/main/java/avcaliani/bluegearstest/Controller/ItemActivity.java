package avcaliani.bluegearstest.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import avcaliani.bluegearstest.R;

public class ItemActivity extends AppCompatActivity {

    protected TextView mTvItem;
    protected TextView mTvItemDesc;
    protected TextView mTvTime;
    protected ImageView mIvIcon;

    protected String mItem = "default";
    protected String mItemDesc = "default";
    protected String mTime = "0000/00/00 00:00:00";
    protected String mImagePath = "default";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        // display back arrow on action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Receiving a intent
        Intent myIntent = this.getIntent();

        try{
            // Receiving number of likes from last activity
            mItem = myIntent.getStringExtra("itemName");
            mItemDesc = myIntent.getStringExtra("itemDesc");
            mTime = getResources().getString(R.string.time_added, myIntent.getStringExtra("time"));
            mImagePath = myIntent.getStringExtra("imagePath");

        }catch (Exception erro){
            Toast.makeText(this, erro.getMessage(), Toast.LENGTH_SHORT).show();
        }

        mTvItem = (TextView) findViewById(R.id.item_tvItem);
        mTvItemDesc = (TextView) findViewById(R.id.item_tvItemDesc);
        mTvTime = (TextView) findViewById(R.id.item_tvTimeAdded);
        mIvIcon = (ImageView) findViewById(R.id.item_ivIcon);

        mTvItem.setText(mItem);
        mTvItemDesc.setText(mItemDesc);
        mTvTime.setText(mTime);

        if (!(mImagePath.equals("default"))){
            try{
                // Minha classe que traz a imagem!
                new ImageFromURL(mIvIcon).execute(mImagePath);
            } catch (Exception error){
                //Log.e ("Erroooo: ", error.getMessage());
                mIvIcon.setImageDrawable(getResources().getDrawable(R.drawable.developer_128px, null));
            }
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
