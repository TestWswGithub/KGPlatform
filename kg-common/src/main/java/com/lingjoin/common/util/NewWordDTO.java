package com.lingjoin.common.util;

public class NewWordDTO {

    private String word;
    private String pos;
    private Double weight;
    private Integer freq;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getFreq() {
        return freq;
    }

    public void setFreq(Integer freq) {
        this.freq = freq;
    }

    public NewWordDTO() {

    }

    @Override
    public String toString() {
        return "NewWordDTO{" +
                "word='" + word + '\'' +
                ", pos='" + pos + '\'' +
                ", weight=" + weight +
                ", freq=" + freq +
                '}';
    }
}
