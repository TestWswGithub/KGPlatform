package com.lingjoin.file.thread;

import com.alibaba.fastjson.JSON;
import com.lingjoin.common.config.MyConfig;
import com.lingjoin.common.util.KeyValJson;
import com.lingjoin.file.entity.KeyVal;
import com.lingjoin.file.service.KeyValService;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class KeyValThread extends Thread {



    private MyConfig myConfig;
    private Integer uid;
    private String uuid;
    private String time;
    private KeyValService keyValService;
    private String kvResult;

    public KeyValThread(MyConfig myConfig, Integer uid, String uuid, String time, KeyValService keyValService, String kvResult) {
        this.myConfig = myConfig;
        this.uid = uid;
        this.uuid = uuid;
        this.time = time;
        this.keyValService = keyValService;
        this.kvResult = kvResult;
    }

    public KeyValThread() {
    }


    @Override
    public void run() {
        if (kvResult==null||"".equals(kvResult)) return;
        KeyValJson keyValJson = JSON.parseObject(kvResult, KeyValJson.class);
        List<KeyValJson.keyVal> keyVals = keyValJson.getKeyVals();
        ArrayList<KeyVal> kvs = new ArrayList<>();
        keyVals.forEach(new Consumer<KeyValJson.keyVal>() {
                            @Override
                            public void accept(KeyValJson.keyVal kv) {
                                if (kv.getId() == -1) return;
                                if (kv.getKey_value() == null || "".equals(kv.getKey_value())) return;
                                if ("emarea".equals(kv.getAttribute())) return;
                                kvs.add(new KeyVal(uid, uuid, time, kv.getName(), kv.getKey_value(), kv.getOrg_para_text()));
                            }
                        });
        keyValService.batchSave(kvs);
    }
}
