package com.lingjoin.graph.gentity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import java.io.Serializable;

@NodeEntity("Entity")
public class Entity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String info;
    private String name;
    //1代表主节点，0代表从节点
    private Integer main;

    public Entity(Long id, String info, String name, Integer main) {
        this.id = id;
        this.info = info;
        this.name = name;
        this.main = main;
    }
    public Integer getMain() {
        return main;
    }

    public void setMain(Integer main) {
        this.main = main;
    }
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Entity() {
    }




}
