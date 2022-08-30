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


public class MainActivity extends AppCompatActivity {

    JSONArray array;
    Adapter adapter;
    ListView list;
    String query="배트만";
    String url="https://openapi.naver.com/v1/search/movie.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);

        //생성된 쓰레드를 실행. 네이버 api결과를 가지고 옴
        new Naver().execute();
        list=findViewById(R.id.list);

    }


    //쓰레드를 생성(네이버 api 써치로 가서 해당 결과값을 가지고옴
    class Naver extends AsyncTask<String ,String, String>{
        @Override
        protected String doInBackground(String... strings) {
            String re=NaverAPI.search(query,url);
            return re;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                array=new JSONObject(s).getJSONArray("items");
                System.out.println("사이즈"+array.length());
                adapter=new Adapter();
                list.setAdapter(adapter);
            }catch (Exception e){
                System.out.println("api가져오기"+e.toString());
            }
            super.onPostExecute(s);
        }
    }
    class Adapter extends BaseAdapter{

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
            View view=getLayoutInflater().inflate(R.layout.item,parent,false);

            try {
                JSONObject obj=(JSONObject) array.get(position);
                String strTitle=obj.getString("title");
                String strActor=obj.getString("actor");
                String strImage=obj.getString("image");
                String strRating=obj.getString("userRating");
                float fltRating=Float.parseFloat(strRating);
                String strDirector=obj.getString("director");
                RatingBar rating=view.findViewById(R.id.rating);
                rating.setRating(fltRating);
                ImageView image=view.findViewById(R.id.image);
                if(!strImage.equals("")){
                    Picasso.get().load(strImage).into(image);



                }
                final String strLink=obj.getString("link");
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,LinkActivity.class);
                        intent.putExtra("link",strLink);
                        startActivity(intent);
                    }
                });


                TextView title=view.findViewById(R.id.title);
                title.setText(Html.fromHtml(strTitle));
                TextView actor=view.findViewById(R.id.actor);
                actor.setText(Html.fromHtml(strActor));
                TextView director=view.findViewById(R.id.director);
                director.setText(Html.fromHtml(strDirector));

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
                new Naver().execute();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.book:
                Intent intent=new Intent(this,bookActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}