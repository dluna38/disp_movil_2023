package com.example.logintaller.models.apiModels;

public class Pelicula {
    public static final String MEDIA_TYPE_SERIE = "tv";
    public static final String MEDIA_TYPE_MOVIE = "movie";
    public static final String MEDIA_TYPE_PERSON = "person";
    public int id;
    public String title;

    public String name;
    public String overview;
    public String poster_path;
    public String media_type;
    public String release_date;
    public String first_air_date;

    public String getReleaseYear(){
        try {
            if(this.media_type.equals(MEDIA_TYPE_MOVIE)){
                return release_date.substring(0,4);
            }else {
                return first_air_date.substring(0,4);
            }
        } catch (Exception e) {
            return "0000";
        }
    }
    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", overview='" + overview + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", media_type='" + media_type + '\'' +
                ", release_date='" + release_date + '\'' +
                ", first_air_date='" + first_air_date + '\'' +
                '}';
    }
}
