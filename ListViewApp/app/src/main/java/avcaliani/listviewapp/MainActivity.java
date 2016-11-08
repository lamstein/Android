package avcaliani.listviewapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //  --  UI  --  //
    protected ListView mUiLista;
    protected EditText mUiTxtNewUser;
    protected Button mUiBtnAdd;
    protected Button mUiBtnRemove;

    protected static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    //  --  Others  --  //
    protected ArrayList<Item> mUserList = new ArrayList<Item>();
    protected ArrayAdapter<Item> mAdapter;

    protected Intent mUserActivityIntent;

    //  --  Database  --  //
    protected DatabaseHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Definindo valores aos Atributos de UI
        mUiTxtNewUser= (EditText) findViewById(R.id.etNewItem);
        mUiBtnAdd = (Button) findViewById(R.id.btnAdd);
        mUiBtnRemove = (Button) findViewById(R.id.btnRemove);
        mUserActivityIntent = new Intent (this, UserActivity.class);
        mUiLista = (ListView) findViewById(R.id.lvLista);

        // Definindo valores aos outros Atributos
        checkPermissionToWriteAndCreateDB();
        // --

        /*
         * LIST
         *
         * Definindo um Adapter soh de Strings
         * First parameter - Context
         * Second parameter - Layout for the row
         * Third parameter - ID of the TextView to which the data is written
         * Forth - the Array of data
         * mAdapter new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mUserList);
         */

        // Populando a Lista com itens default
        populateUserList();
        populateListView();

        // ListView Item Click Listener
        mUiLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item value
                Item  itemValue    = (Item) mUiLista.getItemAtPosition(position);

                mUserActivityIntent.putExtra("username", itemValue.getItemName());
                mUserActivityIntent.putExtra("time", itemValue.getTime());
                startActivity(mUserActivityIntent);
            }

        });

        // Click Listener
        mUiBtnAdd.setOnClickListener(this);
        mUiBtnRemove.setOnClickListener(this);

        hideUIKeyboard();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mDatabase.closeConn();
    }

    @Override
    public void onClick (View v){
        switch(v.getId()){
            case R.id.btnAdd:
                try{
                    addUser(mUiTxtNewUser.getText().toString());
                    mUiTxtNewUser.setText("");
                    hideUIKeyboard();
                } catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRemove:
                try{
                    removeUser(mUiTxtNewUser.getText().toString());
                    mUiTxtNewUser.setText("");
                    hideUIKeyboard();
                } catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected void populateUserList(){
        try{
            Cursor response = mDatabase.getAllData();

            if (response.getCount() <= 0){
                Toast.makeText(this, "Nothing found!", Toast.LENGTH_SHORT).show();
                return;
            }

            while (response.moveToNext()){
                mUserList.add(new Item(response.getString(1), response.getString(2)));
            }
            Collections.sort(mUserList);
        } catch (Exception err){
            Log.e("SQL error! ",err.getMessage());
        }
    }

    protected void populateListView(){
        // ArrayAdapter
        mAdapter = new MyListAdapter();
        // Assign adapter to ListView
        mUiLista.setAdapter(mAdapter);
    }

    protected void addUser(String username) throws Exception{
        if (username == null || username.trim().matches("")){
            throw new Exception("Username can't be null!");
        }

        /*
        // Aqui eu valido pela lista que tenho se o usuario ja existe,
        // mas como o banco ja faz isso nao vou usar esse codigo aqui.

        for(Item user: mUserList){
            if (user.getItemName().equals(username)){
                throw new Exception ("This user already added!");
            }
        }
        */

        try {

            Item newUser = new Item(username.trim());
            boolean isInserted = mDatabase.insertData(newUser.getItemName(), newUser.getTime());

            if (!isInserted){
                throw new Exception("Fail to insert user :(");
            }

            mUserList.add(newUser);
            Collections.sort(mUserList);
            // Update List
            mAdapter.notifyDataSetChanged();
        } catch (Exception err){
            Toast.makeText(this, "Database Error!", Toast.LENGTH_SHORT).show();
            Log.e("SQL error!", err.getMessage());
        }
    }

    protected void removeUser(String username) throws Exception{
        if (username == null || username.trim().matches("")){
            throw new Exception("Username can't be null!");
        }

        for(Item user: mUserList){
            if (user.getItemName().equals(username)){
                try {
                    boolean isDeleted = mDatabase.deleteUserByUsername(user.getItemName());

                    if (!isDeleted){
                        throw new Exception("Fail to remove user :( ");
                    }

                    mUserList.remove(user);
                    // Update List
                    mAdapter.notifyDataSetChanged();
                } catch (Exception err){
                    Toast.makeText(this, "Database Error!", Toast.LENGTH_SHORT).show();
                    Log.e("SQL error!", err.getMessage());
                }
                return;
            }
        }
        throw new Exception("We could not find that user!");
    }

    protected void hideUIKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //
    // Permission
    //


    //  --  Method to Check Permission to Write in a External Storage  --  //
    public void checkPermissionToWriteAndCreateDB (){

        int permissionCheckExt = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // PERMISSION_DENIED  = -1
        // PERMISSION_GRANTED = 0

        try{
            // if I don't have permission, do...
            if (permissionCheckExt == PackageManager.PERMISSION_DENIED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            // if I have...
            else {
                mDatabase = new DatabaseHelper(this);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    } // End Function


    //  --  Method Callback of Permission  --  //
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        // Let's select callback permission
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    mDatabase = new DatabaseHelper(this);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            } // End-Case

            // other 'case' lines to check for other
            // permissions this app might request

        }// End-Switch
    }

    //
    // Private Class MyListAdapter
    //


    // Nesta classe eu crio uma lista de "layouts", com as informacoes da lista de "Itens"
    private class MyListAdapter extends ArrayAdapter<Item>{
        public MyListAdapter(){
            // Contrutor do ArrayAdapter
            // First parameter - Context
            // Second parameter - Layout for the row
            // Third parameter - ID of the TextView to which the data is written

            super(MainActivity.this, R.layout.list_item, mUserList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if (itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }

            Item currentItem = mUserList.get(position);

            // Se quiser mudar a imagem:
            //ImageView imgView = (ImageView)itemView.findViewById(R.id.ivIcon);
            //imgView.setImageDrawable(getResources().getDrawable(R.drawable.user_128px, null)); // Setting up new image

            TextView txtUser = (TextView) itemView.findViewById(R.id.tvUser);
            TextView txtTime = (TextView) itemView.findViewById(R.id.tvTimeAdded);

            txtUser.setText(currentItem.getItemName());
            txtTime.setText(getResources().getString(R.string.time_added, currentItem.getTime()));
            return itemView;
        }
    } // End-Sub Class
}
