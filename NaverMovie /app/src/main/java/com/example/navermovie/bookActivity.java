package com.example.navermovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class bookActivity extends AppCompatActivity {
    String url="https://openapi.naver.com/v1/search/book.json";
    String query="안드로이드";
    JSONArray array;
    ListView list;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("도서검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_book);


        new NaverThread().execute();
        list=findViewById(R.id.list);
    }
    class NaverThread extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String result=NaverAPI.search(query,url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                array=new JSONObject(s).getJSONArray("items");
                BaseAdapter baseAdapter=new BooKAdapter();
                adapter=new BooKAdapter();
                list.setAdapter(adapter);
            }catch (Exception e){
                System.out.println("책검색"+e.toString());
            }
            super.onPostExecute(s);
        }
    }
    class BooKAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return array.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=getLayoutInflater().inflate(R.layout.item1,parent,false);

            try {
                JSONObject obj=(JSONObject) array.get(position);
                String strTitle=obj.getString("title");
                String strAuthor=obj.getString("author");
                String strImage=obj.getString("image");
                String strPrice=obj.getString("discount");


                ImageView image=view.findViewById(R.id.image);
                if(!strImage.equals("")){
                    Picasso.get().load(strImage).into(image);
                }
                final String strLink=obj.getString("link");
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(bookActivity.this,LinkActivity.class);
                        intent.putExtra("link",strLink);
                        startActivity(intent);
                    }
                });

                TextView title=view.findViewById(R.id.title);
                title.setText(Html.fromHtml(strTitle));
                TextView author=view.findViewById(R.id.author);
                author.setText(Html.fromHtml(strAuthor));
                TextView discount=view.findViewById(R.id.discount);
                discount.setText(Html.fromHtml(strPrice));

            } catch (JSONException e) {
                System.out.println("오류"+e.toString());
            }
            return view;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);

        MenuItem search=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query=newText;
                new NaverThread().execute();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.movie:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
