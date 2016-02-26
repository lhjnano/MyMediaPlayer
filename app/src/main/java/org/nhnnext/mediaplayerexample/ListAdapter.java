package org.nhnnext.mediaplayerexample;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by user on 2016-02-01.
 */

public class ListAdapter extends BaseAdapter {

    //  uuid, filename, filepath
    ArrayList<String[]> list = new ArrayList<String[]>();
    Activity activity = null;
    public ListAdapter(Activity activity){
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * 어뎁터가 가지는 모든 뷰들을 리스트로 뿌려주기 위해서 하나씩 각 뷰의 값들을 반환하는 메서드
         * @position : 현재 뷰의 위치 값
         * @convertView : 현재 뷰
         * @convertView : 부모 그룹뷰 (리스트뷰)
         */

        final Context context =  parent.getContext();
        // 1) 객체 지시자 생성
        Holder holder;
        Button button;
        TextView textView;

        // 2) 각 객체가 null 일때의 자기 자신에 관한 구현
        if( convertView == null) {
            // 2-1) 각 레이아웃 불러오기
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_custom, parent, false);
            textView = (TextView)convertView.findViewById( R.id.list_custom_text );
            button = (Button)convertView.findViewById(R.id.list_custom_button);

            // 2-2) 홀더 생성 및 각 멤버 변수 대입
            holder = new Holder();
            holder.textView = textView;
            holder.button = button;
            convertView.setTag(holder);
        }
        // 3) 있던 객체면 위에서 지정한 지시자의 값을 명확히 설정
        else {
            holder = (Holder)convertView.getTag();
            button = holder.button;
            textView = holder.textView;
        }

        // 4) 각 멤버 변수 값 등록
        final String fileName = list.get(position)[1];
        textView.setText(fileName);
        textView.dispatchDisplayHint(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 확장자를 빼고 검색
                MovieAPIAdapter movieAPIAdapter = new MovieAPIAdapter();
                // -- test --
                Log.d("ListAdapter",fileName);
                String name = fileName.split("\\.")[0];
                AsyncTask<String, Void, MovieAPIAdapter.Item> task = movieAPIAdapter.execute(name);
                // 다이얼 로그 생성                           ----------------   2016.02.02 메인 쓰레드 동작 관련 실패
                try {
                    MovieAPIAdapter.Item item = task.get();

                    // 썸네일
                    Log.d("MovieAPIAdapter Dialog", "start");
                    // ---------- 2016.02.25 메인 쓰레드 네트워크 처리 관련 실패 (메인 쓰레드에서는 네트워크를 사용할 수 없다. - 허니컴 이후)
                    // 쓰레드 생성
                    AsyncTask<MovieAPIAdapter.Item, Void , Drawable> asyncTask = new AsyncTask<MovieAPIAdapter.Item, Void, Drawable>() {
                        @Override
                        protected Drawable doInBackground(MovieAPIAdapter.Item... params) {
                            /**
                             *  item중에서 섬네일 주소를 통해 drawable을 생성하고 반환
                             */
                            Drawable drawable = null;
                            try {
                                InputStream inputStream = new URL(params[0].getThumbnail().get(0).getContent()).openStream(); //-- 2016.02.25 err
                                drawable = Drawable.createFromStream(inputStream, null);
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return drawable;
                        }
                    };
                    // 스레드 실행전 값이 유효한지 확인
                    if(item == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("제목이 유효하지 않습니다.");
                        builder.setNegativeButton("닫기",null);
                        builder.create().show();
                        return;
                    }

                    // 스레드 실행
                    asyncTask.execute(item);
                    Drawable drawable = asyncTask.get();

                    // 다이얼로그 레이아웃 불러오기
                    Context mContext = activity.getApplicationContext();
                    LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
                    View convertView = inflater.inflate(R.layout.dialog_theme,null,false);

                    // 각 정보 입력
                    ImageView imageView = (ImageView)convertView.findViewById(R.id.thumbnail_dialog_theme);
                    imageView.setImageDrawable(drawable);

                    TextView year = (TextView)convertView.findViewById(R.id.year_dialog_theme);
                    year.setText(item.getYear().get(0).getContent());

                    TextView story = (TextView)convertView.findViewById(R.id.story_dialog_theme);
                    story.setText(item.getStory().get(0).getContent());

                    TextView nation = (TextView)convertView.findViewById(R.id.nation_dialog_theme);
                    nation.setText(item.getNation().get(0).getContent());

                    TextView genre = (TextView)convertView.findViewById(R.id.genre_dialog_theme);
                    genre.setText(item.getGenre().get(0).getContent());

                    TextView director = (TextView)convertView.findViewById(R.id.director_dialog_theme);
                    director.setText(item.getDirector().get(0).getContent());

                    TextView actor = (TextView)convertView.findViewById(R.id.actor_dialog_theme);
                    actor.setText(item.getActor().get(0).getContent());

                    // 다이얼로그 빌더 생성
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setView(convertView);
                    builder.setTitle(item.getTitle().get(0).getContent());
                    builder.setNegativeButton("닫기" , null );

                    // 다이얼로그 실행
                    builder.create().show();
                    Log.d("MovieAPIAdapter Dialog", "end");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        return convertView;
    }

    public void add(String uuid, String[] fileData) {
        list.add(new String[]{uuid,fileData[0],fileData[1]});
    }

    public void remove(String uuid) {
        for(String[] item : list) {
            if (item[0].equals(uuid)) {
                list.remove(item);
            }
        }
    }

    public ArrayList<String[]> getList(){
        return list;
    }

    private class Holder {
        /**
         *  스크롤 시 데이터가 변경 되는 것과 findViewById()를
         *  사용을 줄여 향상 된 속도를 얻기 위함
         */
        TextView textView;
        Button button;
    }
}