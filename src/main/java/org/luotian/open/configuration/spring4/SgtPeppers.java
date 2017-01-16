package org.luotian.open.configuration.spring4;

import org.springframework.stereotype.Component;

@Component
public class SgtPeppers implements CompactDisc {

    private String title = "Sgt. Pepper's Lonely Hearts Club Band";
    private String artist = "http://blog.csdn.net/unix21";

    public void play() {
        System.out.println("【非常醒目SgtPeppers 】>>>>>>>>>>>>>>>>>Playing " + title + " by " + artist);
    }

}