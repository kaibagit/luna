package org.luna.example.models;

import org.luna.common.persistent.Model;

import javax.persistence.Entity;

/**
 * Created by kaiba on 2016/9/17.
 */
@Entity
public class Member extends Model {

    public String username;

    public String avatar;

}
