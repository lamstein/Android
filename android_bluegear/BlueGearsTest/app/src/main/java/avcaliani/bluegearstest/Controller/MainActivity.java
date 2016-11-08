package avcaliani.bluegearstest.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
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

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import avcaliani.bluegearstest.Model.DatabaseHelper;
import avcaliani.bluegearstest.R;
import avcaliani.bluegearstest.View.Item;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TaskCompleted {

    //  --  UI  --  //
    protected ListView mUiLista;
    protected EditText mUiTxtNewItem;
    protected EditText mUiTxtNewItemDesc;
    protected Button mUiBtnAdd;
    protected Button mUiBtnRemove;

    protected static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public static final String JSON_URL = "http://bpixel.com.br/teste/itens.json";
    protected boolean mFlag = true;
    protected String mJsonData = "";

    //  --  Others  --  //
    protected ArrayList<Item> mItemList = new ArrayList<Item>();
    protected ArrayAdapter<Item> mAdapter;

    protected Intent mItemActivityIntent;

    protected JSONObject mJsonObject;
    protected JSONArray mJsonArray;

    //  --  Database  --  //
    protected DatabaseHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Definindo valores aos Atributos de UI
        mUiTxtNewItem = (EditText) findViewById(R.id.etNewItem);
        mUiTxtNewItemDesc = (EditText) findViewById(R.id.etNewItemDesc);
        mUiBtnAdd = (Button) findViewById(R.id.btnAdd);
        mUiBtnRemove = (Button) findViewById(R.id.btnRemove);
        mItemActivityIntent = new Intent(this, ItemActivity.class);
        mUiLista = (ListView) findViewById(R.id.lvLista);

        // Definindo valores aos outros Atributos
        checkPermissionToWriteAndCreateDB();

        try{
            getJson(JSON_URL);
        } catch (Exception error){
            Log.e("Error!", error.getMessage());
        }

        // ListView Item Click Listener
        mUiLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item value
                Item itemValue = (Item) mUiLista.getItemAtPosition(position);

                mItemActivityIntent.putExtra("itemName", itemValue.getItemName());
                mItemActivityIntent.putExtra("itemDesc", itemValue.getItemDesc());
                mItemActivityIntent.putExtra("time", itemValue.getTime());
                mItemActivityIntent.putExtra("imagePath", itemValue.getImagePath());
                startActivity(mItemActivityIntent);
            }

        });

        // Click Listener
        mUiBtnAdd.setOnClickListener(this);
        mUiBtnRemove.setOnClickListener(this);

        hideUIKeyboard();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                try {
                    addItem(mUiTxtNewItem.getText().toString(), mUiTxtNewItemDesc.getText().toString());
                    mUiTxtNewItem.setText("");
                    mUiTxtNewItemDesc.setText("");
                    hideUIKeyboard();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRemove:
                try {
                    removeItem(mUiTxtNewItem.getText().toString());
                    mUiTxtNewItem.setText("");
                    mUiTxtNewItemDesc.setText("");
                    hideUIKeyboard();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    // Call Back
    @Override
    public void onTaskComplete(String result) {
        try{
            mJsonData = result;
            ArrayList<Item> items = parseJSON(mJsonData);
            for (Item i: items) {
                mDatabase.insertData(i);
            }
            populateItemList();
            populateListView();
        } catch (Exception erro){

        }
    }

    protected void populateItemList() {
        try {
            Cursor response = mDatabase.getAllData();

            if (response.getCount() <= 0) {
                Toast.makeText(this, "Nothing found!", Toast.LENGTH_SHORT).show();
                return;
            }

            while (response.moveToNext()) {
                Item newItem = new Item(response.getString(1), response.getString(2), response.getString(3));
                newItem.setImage(response.getString(4));
                mItemList.add(newItem);
            }
            Collections.sort(mItemList);
        } catch (Exception err) {
            Log.e("SQL error! ", err.getMessage());
        }
    }

    protected void populateListView() {
        // ArrayAdapter
        mAdapter = new MyListAdapter();
        // Assign adapter to ListView
        mUiLista.setAdapter(mAdapter);
    }

    protected void addItem(String itemName, String desc) throws Exception {
        if (itemName == null || itemName.trim().matches("")) {
            throw new Exception("Item name can't be null!");
        }

        if (desc == null || desc.trim().matches("")) {
            throw new Exception("Descriptiom can't be null!");
        }

        try {

            Item newItem = new Item(itemName.trim(), desc.trim());

            boolean isInserted = mDatabase.insertData(newItem);

            if (!isInserted) {
                throw new Exception("Fail to insert item :(");
            }

            mItemList.add(newItem);
            Collections.sort(mItemList);
            // Update List
            mAdapter.notifyDataSetChanged();
        } catch (Exception err) {
            Toast.makeText(this, "Database Error!", Toast.LENGTH_SHORT).show();
            Log.e("SQL error!", err.getMessage());
        }
    }

    protected void removeItem(String itemName) throws Exception {
        if (itemName == null || itemName.trim().matches("")) {
            throw new Exception("Item Name can't be null!");
        }

        for (Item item : mItemList) {
            if (item.getItemName().equals(itemName)) {
                try {
                    boolean isDeleted = mDatabase.deleteItemByName(item.getItemName());

                    if (!isDeleted) {
                        throw new Exception("Fail to remove item :( ");
                    }

                    mItemList.remove(item);
                    // Update List
                    mAdapter.notifyDataSetChanged();
                } catch (Exception err) {
                    Toast.makeText(this, "Database Error!", Toast.LENGTH_SHORT).show();
                    Log.e("SQL error!", err.getMessage());
                }
                return;
            }
        }
        throw new Exception("We could not find that item!");
    }

    protected void hideUIKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //
    // JSON
    //

    public void getJson(String url){
        BackgroundTask bt = new BackgroundTask(this, url);
        bt.execute();
    }

    public ArrayList<Item> parseJSON(String json){

        ArrayList<Item> itemArrayList = new ArrayList<Item>();

        try {
            mJsonObject = new JSONObject(json);
            String data = "default";
            if (mJsonObject.has("data")){
                data = mJsonObject.getString("data");
            }

            if (mJsonObject.has("itens")){
                mJsonArray = mJsonObject.getJSONArray("itens");

                for (int i = 0; i < mJsonArray.length(); i++){

                    JSONObject obj = mJsonArray.getJSONObject(i);
                    Item item = new Item();

                    if (obj.has("nome")){
                        item.setItemName(obj.getString("nome"));
                    }
                    if (obj.has("descricao")){
                        item.setItemDesc(obj.getString("descricao"));
                    }
                    if (obj.has("foto")){
                        item.setImage(obj.getString("foto"));
                    }

                    item.setCurrentTime(data);

                    itemArrayList.add(item);
                } // End-For
            } // End-If

        }catch (Exception error){
            Log.e("Erro!", error.getMessage());
        }

        return itemArrayList;
    } // End-Function



    //
    // Permission
    //


    //  --  Method to Check Permission to Write in a External Storage  --  //
    public void checkPermissionToWriteAndCreateDB() {

        int permissionCheckExt = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // PERMISSION_DENIED  = -1
        // PERMISSION_GRANTED = 0

        try {
            // if I don't have permission, do...
            if (permissionCheckExt == PackageManager.PERMISSION_DENIED) {
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

        } catch (Exception e) {
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
    private class MyListAdapter extends ArrayAdapter<Item> {
        public MyListAdapter() {
            // Contrutor do ArrayAdapter
            // First parameter - Context
            // Second parameter - Layout for the row
            // Third parameter - ID of the TextView to which the data is written

            super(MainActivity.this, R.layout.list_item, mItemList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }

            Item currentItem = mItemList.get(position);

            ImageView imgView = (ImageView) itemView.findViewById(R.id.ivIcon);
            // O img view ja tem uma imagem padrao so se for o caso eu vou trocar
            if (!(currentItem.getImagePath().equals("default"))){
                try{
                    // Minha classe que traz a imagem!
                    new ImageFromURL(imgView).execute(currentItem.getImagePath());
                } catch (Exception error){
                    //Log.e ("Erroooo: ", error.getMessage());
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.developer_128px, null));
                }
            }

            TextView txtItem = (TextView) itemView.findViewById(R.id.tvItem);
            TextView txtItemDesc = (TextView) itemView.findViewById(R.id.tvItemDesc);
            TextView txtTime = (TextView) itemView.findViewById(R.id.tvTimeAdded);

            txtItem.setText(currentItem.getItemName());
            txtItemDesc.setText(currentItem.getItemDesc());
            txtTime.setText(getResources().getString(R.string.time_added, currentItem.getTime()));
            return itemView;
        }
    } // End-Sub Class

}
