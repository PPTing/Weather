package me.ppting.weather;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PPTing on 15/9/22.
 */
public class WeatherInfo
{
    private String error;
    private String status;
    private String date;
    public String getError() {return error;}
    public void  setError(String error) {this.error = error;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getDate(){return date;}
    public void setDate(String date){this.date = date;}


    public List<Results> results = new ArrayList<Results>();
    public List<Results> getResults(){return results;}
    public void setresults(List<Results> results){this.results = results;}
//    public Results results;
//    public Results getResults(){return results;}
//    public void setResults(Results results){this.results = results;}

    public static class Results
    {
        public String currentCity;
        public String pm25;

        public List<Index> getIndex() {
            return index;
        }

        public void setIndex(List<Index> index) {
            this.index = index;
        }

        public List<Index> index = new ArrayList<Index>();

        public List<Weather_data> getWeather_data() {
            return weather_data;
        }

        public void setWeather_data(List<Weather_data> weather_data) {
            this.weather_data = weather_data;
        }

        private List<Weather_data> weather_data = new ArrayList<Weather_data>();
        //get and set
        public String getCurrentCity(){return currentCity;}
        public void setCurrentCity(String currentCity){this.currentCity = currentCity;}
        public String getPm25(){return pm25;}
        public void setPm25(String pm25){this.pm25 = pm25;}

//        public Index getIndex() {
//            return index;
//        }
//
//        public void setIndex(Index index) {
//            this.index = index;
//        }
//
//        public Index index;
//
//        public Weather_data getWeather_data() {
//            return weather_data;
//        }
//
//        public void setWeather_data(Weather_data weather_data) {
//            this.weather_data = weather_data;
//        }
//


        public static class Index
        {
            private String title;
            private String zs;
            private String des;
            private String tipt;

            public String getTitle(){return title;}
            public void setTitle(String title){this.title = title;}
            public String getZs(){return zs;}
            public void setZs(String zs){this.zs = zs;}
            public String getTipt() {return tipt;}
            public void setTipt(String tipt) {this.tipt = tipt;}
            public String getDes() {return des;}
            public void setDes(String des) {this.des = des;}

        }
        public class Weather_data
        {
            private String data;
            private String dayPictureUrl;
            private String nightPictureUrl;
            private String weather;
            private String wind;
            private String temperature;

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getDayPictureUrl() {
                return dayPictureUrl;
            }

            public void setDayPictureUrl(String dayPictureUrl) {
                this.dayPictureUrl = dayPictureUrl;
            }

            public String getNightPictureUrl() {
                return nightPictureUrl;
            }

            public void setNightPictureUrl(String nightPictureUrl) {
                this.nightPictureUrl = nightPictureUrl;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }



        }

    }

}





















