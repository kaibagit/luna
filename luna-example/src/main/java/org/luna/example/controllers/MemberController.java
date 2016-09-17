package org.luna.example.controllers;

import com.avaje.ebean.Ebean;
import org.luna.example.models.Member;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by kaiba on 2016/9/17.
 */
public class MemberController {

    @RequestMapping("new")
    public String new_get(){
        return "members/new_get";
    }

    @RequestMapping("")
    public String index(Model model){
        model.addAttribute("list", Ebean.find(Member.class).findList());
        return "members/index";
    }

    @RequestMapping("create")
    public String create(Member object){
        object.save();
        return "redirect:/members/".concat(object.id.toString());
    }

    @RequestMapping("{id}")
    public String view(Model model, @PathVariable(value="id") Integer id){
        model.addAttribute("object",Ebean.find(Member.class,id));
        return "members/view";
    }

    @RequestMapping("{id}/edit")
    public void edit(Model model,@PathVariable(value="id") Integer id){
        model.addAttribute("object",Ebean.find(Member.class,id));
    }

    @RequestMapping("update")
    public String update(Member object){
        object.update();
        return "redirect:/members/".concat(object.id.toString());
    }

    @RequestMapping("delete")
    public String delete(Integer id){
        Member object = Ebean.find(Member.class,id);
        object.destroy();
        return "redirect:/members";
    }
}
