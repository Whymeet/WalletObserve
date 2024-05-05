package classes.tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tag {
    private Set<String> tags = new HashSet<>();


    public void setTags(String string){
        Collections.addAll(tags, string.split(" "));
    }
    public Set<String> getTags(){
        return tags;
    }



}
