package org.luna.common.persistent;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by kaiba on 2016/9/11.
 */
public abstract class Model extends ActiveRecord {

    @Id
    @GeneratedValue
    public Integer id;
    public Date createTime;
    public Date updateTime;

    public void save(){
        validate();
        this.doBeforeSave();
        super.save();
    }

    public void update(){
        validate();
        this.doBeforeUpdate();
        super.update();
    }

    public void doBeforeSave(){
        Date now = new Date();
        if(createTime == null)
            createTime = now;
        if(updateTime == null)
            updateTime = now;
    }

    public void doBeforeUpdate(){
        if(updateTime == null){
            updateTime = new Date();
        }
    }

    public void validate(){
    }

}
