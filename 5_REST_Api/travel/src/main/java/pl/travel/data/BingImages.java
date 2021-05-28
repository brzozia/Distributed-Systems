package pl.travel.data;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BingImages {
    private List<Image> value;

    public List<Image> getValue() {
        return value;
    }

    public void setValue(List<Image> value) {
        this.value = value;
    }

    public BingImages() {
    }

    public static class Image{
        private String thumbnailUrl;

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public Image() {
        }
    }
}
