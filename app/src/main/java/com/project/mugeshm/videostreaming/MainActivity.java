package com.project.mugeshm.videostreaming;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    //VideoView videoView;
    ListView lv;
    List<String> list;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        getlist();

/*         ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
           lv = (ListView) findViewById(R.id.listView);
           lv.setAdapter(adapter);
           Toast.makeText(getBaseContext(),"List is empty",Toast.LENGTH_SHORT).show();
       }*/

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getlist();
            }
        });
    }

      public List<String> getlist(){
          final List<String> sentence =new ArrayList<String>();
          AsyncHttpClient client = new AsyncHttpClient();
          client.get("http://192.168.0.123:3000/files", null, new AsyncHttpResponseHandler() {
              @Override
              public void onProgress(long bytesWritten, long totalSize) {
                  super.onProgress(bytesWritten, totalSize);
              }

              @Override
              public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                  String str = "";
                  try {
                      str = new String(bytes, "UTF-8");
                  } catch (UnsupportedEncodingException e) {
                      e.printStackTrace();
                  }

                  try {
                      JSONObject obj = new JSONObject(str);

                      JSONArray arr = obj.getJSONArray("files");
                      //Toast.makeText(getBaseContext(), "arr: " + arr.toString(), Toast.LENGTH_LONG).show();
                      for (int i = 0; i < arr.length(); i++) {
                          String filename = arr.get(i).toString();
                          sentence.add(filename);
                          //Toast.makeText(getBaseContext(), "files: " +filename , Toast.LENGTH_LONG).show();
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }

                  // Toast.makeText(getBaseContext(), "files: " + str, Toast.LENGTH_LONG).show();
                  Log.d("success", String.valueOf(statusCode));

                /*  LinearLayout ll = (LinearLayout) findViewById(R.id.rl);
                  ListView lv = new ListView(getBaseContext());
                  ll.addView(lv);
                  setContentView(ll);
                  ArrayAdapter adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, sentence);
                  lv.setAdapter(adapter);*/
                  createlist(sentence);
                  Toast.makeText(getBaseContext(), "Successfully fetched list from server", Toast.LENGTH_LONG).show();

              }

              @Override
              public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                  // handle failure response
                  Toast.makeText(getBaseContext(), "Error fetching data from server", Toast.LENGTH_LONG).show();
                  Log.d("Failure", String.valueOf(statusCode));

              }

          });


          return sentence;
      }

    public void createlist(List<String> fileslist){

        ArrayAdapter adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1,fileslist);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String itemValue = (String) lv.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();

                Intent myIntent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                myIntent.putExtra("videoname", itemValue);
                startActivity(myIntent);
            }
        });
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
