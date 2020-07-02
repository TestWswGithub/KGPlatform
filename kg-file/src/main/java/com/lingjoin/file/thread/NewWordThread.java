package com.lingjoin.file.thread;

import com.alibaba.fastjson.JSON;
import com.lingjoin.common.config.MyConfig;
import com.lingjoin.common.jna.NWF;
import com.lingjoin.common.util.NewWordDTO;
import com.lingjoin.file.entity.NewWord;
import com.lingjoin.file.service.NewWordService;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NewWordThread extends Thread {

    private MyConfig myConfig;
    private NewWordService newWordService;
    private Integer uid;
    private String uuid;
    private String fromDocName;
    private String time;
    private String text;


    public NewWordThread(NewWordService newWordService, MyConfig myConfig, Integer uid, String uuid, String fromDocName, String time, String text) {
        this.myConfig = myConfig;
        this.uid = uid;
        this.uuid = uuid;
        this.fromDocName = fromDocName;
        this.time = time;
        this.text = text;
        this.newWordService = newWordService;
    }

    public NewWordThread() {

    }

    @Override
    public void run() {

        //String newWords = NWF.Instance.NWF_GetFileNewWords(path, 50, true);
        String newWords = NWF.Instance.NWF_GetNewWords(text, 50, true);
        if (newWords != null && !"".equals(newWords)) {
            List<NewWordDTO> newWordDTOS = JSON.parseArray(newWords, NewWordDTO.class);
            if (newWordDTOS != null && newWordDTOS.size() != 0) {
                ArrayList<NewWord> words = new ArrayList<>();
                for (NewWordDTO word : newWordDTOS) {
                    NewWord newWord = new NewWord();
                    newWord.setName(word.getWord());
                    newWord.setInfoe(String.valueOf(word.getWeight()));
                    newWord.setFeq(word.getFreq());
                    newWord.setTime(time);
                    newWord.setUuid(uuid);
                    newWord.setUid(uid);
                    try {
                        newWord.setCreateDate(new SimpleDateFormat("yyyyMMddHHmmss").parse(time));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    newWord.setFromDocName(fromDocName);
                    words.add(newWord);
                }
                newWordService.batchSave(words);
            }
        }


    }
}
