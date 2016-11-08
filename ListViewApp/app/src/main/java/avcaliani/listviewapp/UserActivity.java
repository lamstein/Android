package avcaliani.listviewapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    protected String mUsername = "null";
    protected String mTime = "null";

    protected TextView mTvUser;
    protected TextView mTvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // display back arrow on action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Receiving a intent
        Intent myIntent = this.getIntent();

        try{
            // Receiving number of likes from last activity
            mUsername = myIntent.getStringExtra("username");
            mTime = getResources().getString(R.string.time_added, myIntent.getStringExtra("time"));

        }catch (Exception erro){
            Toast.makeText(this, erro.getMessage(), Toast.LENGTH_SHORT).show();
        }

        mTvUser = (TextView) findViewById(R.id.user_tvUser);
        mTvTime = (TextView) findViewById(R.id.user_tvTimeAdded);

        mTvUser.setText(mUsername);
        mTvTime.setText(mTime);

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
