package org.nhnnext.mediaplayerexample;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

import java.io.IOException;
import java.util.List;

/**
 * Simple example for the <a href="http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json">
 * 다음 영화 목록 검색 API </a>.
 *
 * @author Yaniv Inbar
 * @modify Heon-Je Lee
 */
public class MovieAPIAdapter extends AsyncTask<String, Void, MovieAPIAdapter.Item> {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    //  한국영화진흥원 APi
    //static final String targetUrl  = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";
    //static final String access_token = "aa50ed3f06f77b78abbb909ec454420d";

    // 다음영화 API
    static final String access_token = "35c6e4670ed6e00beaaa2a8f22feefe2";
    static final String output = "json";
    static final String targetUrl = "https://apis.daum.net/contents/movie";

    @Override
    protected Item doInBackground(String... params) {
        /**
         * 다음 영화 API통하여 영화를 검색하고 VedioListActivity로 다이얼로그를 통해
         * 내용을 보여주기 위해 itemList 전송
         *
         * @param params[0] 검색할 파일 이름
         * @param params[1] 데이터를 전송할 액티비티 (액티비티만 데이터 전송가능)
         */
        // api를 받아 전송할 itemList
        List<Item> itemList = null;

        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    // 파싱 형태를 결정
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });
        // 검색어 입력 여부 확인
        if(params.length == 0) {
            Log.d("MovieAPIAdapter","No searched MovieName");
            return null;
        }

        // json 형식으로 들어오는 URL를 지정
        class MUrl extends GenericUrl {
            public MUrl(String URL) {
                super(URL);
            }
            @Key
            String apikey;
            @Key
            String output;
            @Key    // 검색어
            String q;
        }

        MUrl mUrl = new MUrl(targetUrl);
        mUrl.apikey = access_token;
        mUrl.output = output;

        // 검색어 입력
        mUrl.q = params[0].toString(); // !!  프로젝트 자체 인코딩 할 것

        // request 객체 생성
        HttpRequest request = null;
        try {
            request = requestFactory.buildGetRequest(mUrl);     // IOException
        } catch (IOException e) {
            Log.d("MovieAPIAdapter", "IOException first");
        }


        // 실행하고 결과 값 도출
        try {
            String result = request.execute().parseAsString();
            // Log.d("MovieAPIAdapter", result);
            MovieInfo movieInfo = request.execute().parseAs(MovieInfo.class);
            Channel channel = movieInfo.getChannel();

            if (channel.getResult() != 10) {      //   API 에러
                Log.d("MovieAPIAdapter", "Error.");
                return null;
            }


            if (channel.getTotalCount() == 0) {  // 검색 결과 없음
                Log.d("MovieAPIAdapter", "No videos found.");
                return null;
            }
            // test
            Log.d("MovieAPIAdapter", "total : " + channel.getTotalCount());
            itemList = channel.getItem();
            for (Item item : itemList) {
                Log.d("MovieAPIAdapter", "-----------------------------------------------");
                Log.d("MovieAPIAdapter", "Title: " + item.getTitle().get(0).getContent());
                Log.d("MovieAPIAdapter", "Genre: " + item.getGenre().get(0).getContent());
                Log.d("MovieAPIAdapter", "Thumbnail: " + item.getThumbnail().get(0).getContent());
            }
        } catch (IOException e) {
            Log.d("MovieAPIAdapter", "IOException second");
        }

        // VideoListActivity로 데이터 전송
        return itemList.get(0);
    }


    /**
     * 파싱될 객체
     */
    public static class MovieInfo {
        @Key
        private Channel channel;

        public Channel getChannel() {
            return channel;
        }
        public void setChannel(Channel channel) {
            this.channel = channel;
        }
    }

    public static class Channel {
        @Key
        private int result;
        @Key
        private int totalCount;
        @Key
        private String q;
        @Key
        private List<Item> item;

        public String getQ() {
            return q;
        }

        public void setQ(String q) {
            this.q = q;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<Item> getItem() {
            return item;
        }

        public void setItem(List<Item> item) {
            this.item = item;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }
    }

   public static class Item{
        @Key
        private List<Content> genre;
        @Key
        private List<Content> res;
        @Key
        private List<Content> trailer;
        @Key
        private List<Content> actor;
        @Key
        private List<Content> story;
        @Key
        private List<Content> title;
        @Key
        private List<Content> vedio_info;
        @Key
        private List<Content> open_info;
        @Key
        private List<Content> director;
        @Key
        private List<Content> year;
        @Key
        private List<Content> thumbnail;
        @Key
        private List<Content> nation;
        @Key
        private List<Content> grade1;
        @Key
        private List<Content> grade2;
        @Key
        private List<Content> grade3;
        @Key
        private List<Content> expect_grades;

       public List<Content> getActor() {
            return actor;
        }

        public void setActor(List<Content> actor) {
            this.actor = actor;
        }

        public List<Content> getDirector() {
            return director;
        }

        public void setDirector(List<Content> director) {
            this.director = director;
        }

        public List<Content> getExpect_grades() {
            return expect_grades;
        }

        public void setExpect_grades(List<Content> expect_grades) {
            this.expect_grades = expect_grades;
        }

        public List<Content> getGenre() {
            return genre;
        }

        public void setGenre(List<Content> genre) {
            this.genre = genre;
        }

        public List<Content> getGrade1() {
            return grade1;
        }

        public void setGrade1(List<Content> grade1) {
            this.grade1 = grade1;
        }

        public List<Content> getGrade2() {
            return grade2;
        }

        public void setGrade2(List<Content> grade2) {
            this.grade2 = grade2;
        }

        public List<Content> getGrade3() {
            return grade3;
        }

        public void setGrade3(List<Content> grade3) {
            this.grade3 = grade3;
        }

        public List<Content> getNation() {
            return nation;
        }

        public void setNation(List<Content> nation) {
            this.nation = nation;
        }

        public List<Content> getOpen_info() {
            return open_info;
        }

        public void setOpen_info(List<Content> open_info) {
            this.open_info = open_info;
        }

        public List<Content> getRes() {
            return res;
        }

        public void setRes(List<Content> res) {
            this.res = res;
        }

        public List<Content> getStory() {
            return story;
        }

        public void setStory(List<Content> story) {
            this.story = story;
        }

        public List<Content> getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(List<Content> thumbnail) {
            this.thumbnail = thumbnail;
        }

        public List<Content> getTitle() {
            return title;
        }

        public void setTitle(List<Content> title) {
            this.title = title;
        }

        public List<Content> getTrailer() {
            return trailer;
        }

        public void setTrailer(List<Content> trailer) {
            this.trailer = trailer;
        }

        public List<Content> getVedio_info() {
            return vedio_info;
        }

        public void setVedio_info(List<Content> vedio_info) {
            this.vedio_info = vedio_info;
        }

        public List<Content> getYear() {
            return year;
        }

        public void setYear(List<Content> year) {
            this.year = year;
        }

   }
    public static class Content{
        @Key
        private String content;
        @Key
        private String link;

        public String getLink() {
            return link;
        }
        public void setLink(String link) {
            this.link = link;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
    }
}
