package com.lingjoin.web.util;

import java.io.Serializable;

public class NewRDTO  implements Serializable {


    private String source;
    private String target;
    private String name;
    private String des;
    private String sourceId;
    private String targetId;
    private LineStyle lineStyle;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public LineStyle getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public NewRDTO(String source, String target, String name, String des, String sourceId, String targetId) {
        this.source = source;
        this.target = target;
        this.name = name;
        this.des = des;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    static class LineStyle{

        private Normal normal;


        public Normal getNormal() {
            return normal;
        }

        public void setNormal(Normal normal) {
            this.normal = normal;
        }

        public LineStyle() {

        }

        public LineStyle(Normal normal) {

            this.normal = normal;
        }

        static class Normal{
            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            private String color;

            public Normal() {
            }

            public Normal(String color) {

                this.color = color;
            }
        }

    }
}
