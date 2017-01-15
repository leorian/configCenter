package org.luotian.open.configuration.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

/**
 * Created by xiezhonggui on 17-1-15.
 */
public class Tester {

    public static void main(String args[]) {
        ApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        multicaster.addApplicationListener(new MyStartListener());
        multicaster.addApplicationListener(new MyEndListener());
        System.out.println("--1--");
        multicaster.multicastEvent(new MyStartEvent(""));
        System.out.println("--2--");
        multicaster.multicastEvent(new MyEndEvent(""));
    }

    public static class MyStartEvent extends ApplicationEvent {

        public MyStartEvent(Object source) {
            super(source);
        }
    }

    public static class MyEndEvent extends ApplicationEvent {

        public MyEndEvent(Object source) {
            super(source);
        }
    }

    public static class MyStartListener implements ApplicationListener {

        public void onApplicationEvent(ApplicationEvent applicationEvent) {
            if (applicationEvent instanceof MyStartEvent) {
                System.out.println("Programmer start Action Listener!");
            }
        }
    }

    public static class MyEndListener implements ApplicationListener {

        public void onApplicationEvent(ApplicationEvent applicationEvent) {
            if (applicationEvent instanceof MyEndEvent) {
                System.out.println("Programmer end Action Listener!");
            }
        }
    }
}
