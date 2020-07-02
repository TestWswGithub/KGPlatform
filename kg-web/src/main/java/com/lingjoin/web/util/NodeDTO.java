package com.lingjoin.web.util;

import java.io.Serializable;

public class NodeDTO implements Serializable {

    private String name;
    private String des;
    private String nodeId;
    private Integer level;
    private ItemStyle itemStyle;

    public ItemStyle getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(ItemStyle itemStyle) {
        this.itemStyle = itemStyle;
    }




    public NodeDTO(String name, String des, String nodeId, Integer level) {
        this.name = name;
        this.des = des;
        this.nodeId = nodeId;
        this.level = level;
    }

    public Integer getLevel() {

        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public NodeDTO() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
