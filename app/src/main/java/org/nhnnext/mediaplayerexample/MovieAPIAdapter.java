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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Simple example for the <a href="http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json">
 * 한국영화진흥원 영화 목록 검색 API </a>.
 *
 * @author Yaniv Inbar
 * @modify Heon-Je Lee
 */
public class MovieAPIAdapter extends AsyncTask{

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    static final String targetUrl  = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";
    static final String access_token = "aa50ed3f06f77b78abbb909ec454420d";

    @Override
    protected Object doInBackground(Object[] params) {

        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    // 파싱 형태를 결정
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });

        // json 형식으로 들어오는 URL를 지정
        MovieDBUrl url = new MovieDBUrl( targetUrl );
        url.setKey( access_token );


        // 검색어 입력
        try {
            url.openStartDt = URLEncoder.encode("1990","utf-8");                   // UnsupportedEncodingException
        } catch (UnsupportedEncodingException e1) {
            Log.d("___MovieAPIAdapter", "UnsupportedEncodingException");
        }

        // request 객체 생성
        HttpRequest request = null;
        try {
            request = requestFactory.buildGetRequest(url);                              // IOException
        }catch (IOException e){
            Log.d("MovieAPIAdapter","IOException first");
        }

        // 실행하고 결과 값 도출
        try{
            String result = request.execute().parseAsString();
            Log.d("MovieAPIAdapter",result);
            Packet pac = request.execute().parseAs(Packet.class);           // IOException
            MovieList list = pac.movieListResult;
            Log.d("MovieAPIAdapter",""+list.totCnt);
            Log.d("MovieAPIAdapter",""+list.source);

            if (list.totCnt == 0) {
                Log.d("MovieAPIAdapter","No videos found.");
            } else {
                Log.d("MovieAPIAdapter",list.movieList.size() + " videos found:");
                for (Movie movie : list.movieList) {
                    Log.d("MovieAPIAdapter","-----------------------------------------------");
                    Log.d("MovieAPIAdapter", "Title: " + movie.movieNm);
                    Log.d("MovieAPIAdapter", "ID: " + movie.nationAlt);
                }
            }
        }catch (IOException e){
            Log.d("MovieAPIAdapter","IOException second");
        }

        return null;
    }



    /**
     * Represents a JSON Datas Formatting
     */

    public static class Packet {
        @Key
        private MovieList movieListResult;

        // get set
        public MovieList getMovieListResult() {
            return movieListResult;
        }

        public void setMovieListResult(MovieList movieListResult) {
            this.movieListResult = movieListResult;
        }
    }

    public static class MovieList {
        @Key
        private int totCnt;
        @Key
        private String source;
        @Key
        private List<Movie> movieList;

        public String getSource() {
            return source;
        }

        // getset
        public int getTotCnt() {
            return totCnt;
        }

        public void setTotCnt(int totCnt) {
            this.totCnt = totCnt;
        }
        public void setSource(String source) {
            this.source = source;
        }
        public List<Movie> getMovieList() {
            return movieList;
        }

        public void setMovieList(List<Movie> movieList) {
            this.movieList = movieList;
        }
    }

    public static class Movie {
        @Key
        private String movieCd;
        @Key
        private String movieNm;
        @Key
        private String movieNmEn;
        @Key
        private String prdtYear;
        @Key
        private String openDt;
        @Key
        private String typeNm;
        @Key
        private String prdtStatNm;
        @Key
        private String nationAlt;
        @Key
        private String genreAlt;
        @Key
        private String repNationNm;
        @Key
        private String repGenreNm;
        @Key
        private List<Director> directors;
        @Key
        private List<Company> companys;

        // get set
        public List<Company> getCompanys() {
            return companys;
        }

        public void setCompanys(List<Company> companys) {
            this.companys = companys;
        }

        public List<Director> getDirectors() {
            return directors;
        }

        public void setDirectors(List<Director> directors) {
            this.directors = directors;
        }

        public String getGenreAlt() {
            return genreAlt;
        }

        public void setGenreAlt(String genreAlt) {
            this.genreAlt = genreAlt;
        }

        public String getMovieCd() {
            return movieCd;
        }

        public void setMovieCd(String movieCd) {
            this.movieCd = movieCd;
        }

        public String getMovieNm() {
            return movieNm;
        }

        public void setMovieNm(String movieNm) {
            this.movieNm = movieNm;
        }

        public String getMovieNmEn() {
            return movieNmEn;
        }

        public void setMovieNmEn(String movieNmEn) {
            this.movieNmEn = movieNmEn;
        }

        public String getNationAlt() {
            return nationAlt;
        }

        public void setNationAlt(String nationAlt) {
            this.nationAlt = nationAlt;
        }

        public String getOpenDt() {
            return openDt;
        }

        public void setOpenDt(String openDt) {
            this.openDt = openDt;
        }

        public String getPrdtStatNm() {
            return prdtStatNm;
        }

        public void setPrdtStatNm(String prdtStatNm) {
            this.prdtStatNm = prdtStatNm;
        }

        public String getPrdtYear() {
            return prdtYear;
        }

        public void setPrdtYear(String prdtYear) {
            this.prdtYear = prdtYear;
        }

        public String getRepGenreNm() {
            return repGenreNm;
        }

        public void setRepGenreNm(String repGenreNm) {
            this.repGenreNm = repGenreNm;
        }

        public String getRepNationNm() {
            return repNationNm;
        }

        public void setRepNationNm(String repNationNm) {
            this.repNationNm = repNationNm;
        }

        public String getTypeNm() {
            return typeNm;
        }

        public void setTypeNm(String typeNm) {
            this.typeNm = typeNm;
        }
    }

    public static class Director {
        @Key
        private String peopleNm;
        //get set
        private String getPeopleNm() {
            return peopleNm;
        }

        public void setPeopleNm(String peopleNm) {
            this.peopleNm = peopleNm;
        }
    }

    public static class Company {
        @Key
        private String companyCd;
        @Key
        private String companyNm;
        //get set
        public String getCompanyCd() {
            return companyCd;
        }

        public void setCompanyCd(String companyCd) {
            this.companyCd = companyCd;
        }

        public String getCompanyNm() {
            return companyNm;
        }

        public void setCompanyNm(String companyNm) {
            this.companyNm = companyNm;
        }
    }

    /**
     * URL for 한국영화진흥원 API.
     */
    public static class MovieDBUrl extends GenericUrl {

        public MovieDBUrl(String encodedUrl) {
            super(encodedUrl);
        }

        @Key
        private String key;
        @Key
        private String curPage;
        @Key
        private String itemPerPage ;
        @Key
        private String movieNm;
        @Key
        private String directorNm;
        @Key
        private String openStartDt;

        // get set
        public String getCurPage() {
            return curPage;
        }

        public void setCurPage(String curPage) {
            this.curPage = curPage;
        }

        public String getDirectorNm() {
            return directorNm;
        }

        public void setDirectorNm(String directorNm) {
            this.directorNm = directorNm;
        }

        public String getItemPerPage() {
            return itemPerPage;
        }

        public void setItemPerPage(String itemPerPage) {
            this.itemPerPage = itemPerPage;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getMovieNm() {
            return movieNm;
        }

        public void setMovieNm(String movieNm) {
            this.movieNm = movieNm;
        }

        public String getOpenStartDt() {
            return openStartDt;
        }

        public void setOpenStartDt(String openStartDt) {
            this.openStartDt = openStartDt;
        }
    }
}