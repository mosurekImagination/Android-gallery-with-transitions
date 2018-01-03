package net.mosur.tomasz.pam11;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView gv;
    ArrayList<Integer> resIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadImages();


        setContentView(R.layout.activity_main);
        gv = (GridView)findViewById(R.id.gridview);
        gv.setAdapter( new GridViewAdapter());

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),view_image.class)
                        .putIntegerArrayListExtra("idList", resIds);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

       gv.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                if(me.getAction() == android.view.MotionEvent.ACTION_UP)
                gv.smoothScrollToPosition(gv.getFirstVisiblePosition());
                return false;
            }});

    }

    private void loadImages()
    {
        resIds = new ArrayList<>();
        for(int i=0; i<3; i++) {
            String name = "a" + String.valueOf(i);
            int resId = getResources().getIdentifier(name, "drawable", getPackageName());
            System.out.print(resId + " " + name);
            resIds.add(resId);
        }

    }
    class GridViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return resIds.size()*10;
        }

        @Override
        public Object getItem(int position) {
            return resIds.get(position%3);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           convertView = getLayoutInflater().inflate(R.layout.single_grid, parent, false);
            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView);
            iv.setImageResource(resIds.get(position%3));
            return convertView;
        }
    }

}
