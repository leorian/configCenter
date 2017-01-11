package org.luotian.open.configuration.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11 0011.
 */
public class HelloVelocity {

    public static void main(String args[]) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        Template t = velocityEngine.getTemplate("hellovelocity.vm");

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("name", "velocity");
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        velocityContext.put("list", list);
        StringWriter stringWriter = new StringWriter();
        t.merge(velocityContext, stringWriter);
        System.out.println(stringWriter.toString());
    }
}
