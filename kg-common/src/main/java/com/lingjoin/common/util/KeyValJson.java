package com.lingjoin.common.util;

import java.util.List;

public class KeyValJson {

    public static class keyVal{

        private Integer id;
        private String attribute;//emarea
        private String key_value;
        private String name;
        private Integer offset;
        private String org_para_text;
        private String para_id;
        private String rule_used;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getKey_value() {
            return key_value;
        }

        public void setKey_value(String key_value) {
            this.key_value = key_value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public String getOrg_para_text() {
            return org_para_text;
        }

        public void setOrg_para_text(String org_para_text) {
            this.org_para_text = org_para_text;
        }

        public String getPara_id() {
            return para_id;
        }

        public void setPara_id(String para_id) {
            this.para_id = para_id;
        }

        public String getRule_used() {
            return rule_used;
        }

        public void setRule_used(String rule_used) {
            this.rule_used = rule_used;
        }

        public keyVal() {
        }
    }
    private List<keyVal> KeyVals;

    public List<keyVal> getKeyVals() {
        return KeyVals;
    }

    public void setKeyVals(List<keyVal> keyVals) {
        KeyVals = keyVals;
    }

    public KeyValJson() {

    }
}
