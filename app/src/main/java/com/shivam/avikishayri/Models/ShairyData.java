package com.shivam.avikishayri.Models;

public class ShairyData {
    String shair,shairy,audio;

    public ShairyData() {
    }

    public ShairyData(String shair, String shairy, String audio) {
        this.shair = shair;
        this.shairy = shairy;
        this.audio = audio;
    }

    public String getShair() {
        return shair;
    }

    public void setShair(String shair) {
        this.shair = shair;
    }

    public String getShairy() {
        return shairy;
    }

    public void setShairy(String shairy) {
        this.shairy = shairy;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
