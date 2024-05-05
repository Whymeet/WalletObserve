package classes.tags;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Tag {
    private Set<String> tags = new HashSet<>();


    public void addTags(String string){
        Collections.addAll(tags, string.split(" "));
        tags.remove("");

    }
    public void setTags(String string){
        tags.clear();
        Collections.addAll(tags, string.split(" "));
        tags.remove("");

    }
    public Set<String> getTags(){
        return tags;
    }



}
