package avcaliani.toolbarsearch;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> mNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.listView);
        populateNamesList();
        mAdapter = new ArrayAdapter<String>(this, R.layout.item, mNames);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // use this method when query submitted
                Toast.makeText(MainActivity.this, "End of Search!", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // use this method for auto complete search process

                mAdapter.clear();

                if (newText == null || newText.trim().matches("")){
                    mAdapter.addAll(mNames);
                } else {
                    mAdapter.addAll(searchNames(newText));
                }
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateNamesList(){
        mNames = new ArrayList<String>();
        mNames.add("Anthony");
        mNames.add("Anderson");
        mNames.add("Junior");
        mNames.add("Julho");
        mNames.add("Zelha");
    }

    private List<String> searchNames(String search){
        StringTokenizer st;
        List<String> sNames = new ArrayList<String>();

        for (String name : mNames){
            st = new StringTokenizer(name.toLowerCase(), search.toLowerCase(), true);

            if (!name.toLowerCase().equals(search.toLowerCase()) && st.countTokens() <= 1)
                continue;
            sNames.add(name);
        }
        return sNames;
    }
}
