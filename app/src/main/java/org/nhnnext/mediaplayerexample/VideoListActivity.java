package org.nhnnext.mediaplayerexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by user on 2016-02-01.
 */
public class VideoListActivity extends AppCompatActivity {
    // 비디오 파일 관리자
    private FilePathManager filePathManager;
    // 비디오 리스트 어뎁터
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vediolist);

        // 1.  리스트어뎁터 생성
        listAdapter  = new ListAdapter(this);

        // 2. 메인 레이아웃 연결
        ListView listView = (ListView) findViewById(R.id.activity_vediolist_listview);

        // 3. listview와 adapter 연결
        listView.setAdapter(listAdapter);

        // 4. listview에 어뎁터를 통하여 item 추가
        filePathManager = FilePathManager.getInstance();

        HashMap<String, String[]> fileMap = filePathManager.getFileMap();
        for(String uuid : fileMap.keySet()){
            listAdapter.add(uuid, fileMap.get(uuid));
        }

        // 5. 각 뷰의 리스너 등록
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 파일리스트뷰를 클릭시 해당파일을 볼 수 있는 어플리케이션 소개
                 */
                final TextView textView = (TextView) view.findViewById(R.id.list_custom_text);
                Toast.makeText(getApplicationContext(), textView.getText(), Toast.LENGTH_LONG).show();
                String[] item = (String[]) listAdapter.getItem(position);
                String uuid = item[0];

                // MEDIA PLAY   - 내장 동영상 재생기를 통한 통영상 스트리밍
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(item[2]);
                intent.setDataAndType(uri,"video/*");
                startActivity(intent);

                // custom video
                /*
                Intent intent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uuid", uuid);
                intent.putExtras(bundle);
                startActivity(intent);
                */
            }
        });

        for(int i =0 ; i< listAdapter.getCount(); i++ ) {
            listAdapter.getItem(i);
        }
    }
}
