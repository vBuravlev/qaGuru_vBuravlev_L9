package guru.qa.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeacherJackson {
    public String name;
    public String surname;
    @JsonProperty("favorite_music")
    public List<String> favoriteMusic;
    public Address address;
}
