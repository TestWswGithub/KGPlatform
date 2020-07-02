package com.lingjoin.graph.gentity;

import org.neo4j.ogm.annotation.*;

import java.io.Serializable;

@RelationshipEntity("Relationship")
public class Relationship implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Property("name")
    private String name;
    @Property("weight")
    private Integer weight;

    @StartNode
    private Entity from;
    @EndNode
    private Entity to;


    @Override
    public String toString() {
        return "R{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", from=" + from +
                ", to=" + to +
                '}';
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Entity getFrom() {
        return from;
    }

    public void setFrom(Entity from) {
        this.from = from;
    }

    public Entity getTo() {
        return to;
    }

    public void setTo(Entity to) {
        this.to = to;
    }

    public Relationship() {
    }

    public Relationship(Long id, String name, Integer weight, Entity from, Entity to) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.from = from;
        this.to = to;
    }





}
