package classes.tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tag {
    private List<String> tags = new ArrayList<>();


    public void setTags(String string){
        Collections.addAll(tags, string.split(" "));
    }
    public List<String> getTags(){
        return tags;
    }



}
