package org.luotian.open.configuration.domain;

import java.io.Serializable;

/**
 * Created by xiezhonggui on 17-1-15.
 */
public class Greeting implements Serializable{
    private long id;
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }
}
