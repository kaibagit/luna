package org.luna.common.persistent;

import com.avaje.ebean.Ebean;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by kaiba on 2016/9/11.
 */
public abstract class ActiveRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    public String reflectionToString(){
        return ToStringBuilder.reflectionToString(this);
    }

    public void save(){
        Ebean.save(this);
    }

    public void update(){
        Ebean.save(this);
    }

    public void destroy(){
        Ebean.delete(this);
    }
}
