package com.lingjoin.common.util;

import java.util.List;

public class JsonResult {

    public static class ETuple{
        private String attribute;
        private String entity;
        private String val;

        @Override
        public String toString() {
            return "ETuple{" +
                    "attribute='" + attribute + '\'' +
                    ", entity='" + entity + '\'' +
                    ", val='" + val + '\'' +
                    '}';
        }

        public ETuple() {
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }
    public static class RTuple{
        private String head;
        private String relation;
        private String tail;

        @Override
        public String toString() {
            return "RTuple{" +
                    "head='" + head + '\'' +
                    ", relation='" + relation + '\'' +
                    ", tail='" + tail + '\'' +
                    '}';
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getTail() {
            return tail;
        }

        public void setTail(String tail) {
            this.tail = tail;
        }

        public RTuple() {
        }
    }
    private List<ETuple> entity_attribute;
    private List<RTuple> relation;

    public List<ETuple> getEntity_attribute() {
        return entity_attribute;
    }

    public void setEntity_attribute(List<ETuple> entity_attribute) {
        this.entity_attribute = entity_attribute;
    }

    public List<RTuple> getRelation() {
        return relation;
    }

    public void setRelation(List<RTuple> relation) {
        this.relation = relation;
    }

    public JsonResult() {
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "entity_attribute=" + entity_attribute +
                ", relation=" + relation +
                '}';
    }
}
