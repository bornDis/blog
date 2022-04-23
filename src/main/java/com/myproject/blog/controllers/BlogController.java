package com.myproject.blog.controllers;

import com.myproject.blog.models.Post;
import com.myproject.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMain(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("postname",posts);
        return "blog-main";
    }

    @GetMapping("blog/add")
    public String blogAdd(Model model){
        return "blog-add";
    }

    @PostMapping("blog/add")
    public String blogAdd(@RequestParam String title, @RequestParam String announce,
                              @RequestParam String full_text, Model model){
        Post post = new Post(title,announce,full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{idn}")
    public String blogDetails(@PathVariable(value = "idn") Long ids, Model model){
        if(!postRepository.existsById(ids)){
            return "redirect:/blog";
        }

        Optional<Post> pp = postRepository.findById(ids);
        ArrayList<Post> arrayList = new ArrayList();
        pp.ifPresent(arrayList::add);
        model.addAttribute("postname2",arrayList);
        return "blog-details";
    }

    @GetMapping("/blog/{idn}/edit")
    public String blogEdit(@PathVariable (value = "idn") Long ids,Model model){
        Optional<Post> post = postRepository.findById(ids);
        ArrayList<Post> arpost = new ArrayList<>();
        post.ifPresent(arpost::add);
        model.addAttribute("post3",arpost);
        return "blog-update";
    }

    @PostMapping("/blog/{idn}/edit")
    public String blogPostUpdate(@PathVariable (value = "idn") Long ids, @RequestParam String title,
                           @RequestParam String full_text, @RequestParam String announce, Model model){
        Post post = postRepository.findById(ids).orElseThrow();
        post.setTitle(title);
        post.setAnnounce(announce);
        post.setFull_text(full_text);

        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{idn}/delete")
    public String blogPostDelete(@PathVariable (value = "idn") Long ids,Model model){
        Post post = postRepository.findById(ids).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }
}
