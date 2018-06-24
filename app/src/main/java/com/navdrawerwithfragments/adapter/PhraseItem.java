package com.navdrawerwithfragments.adapter;

public class PhraseItem {
    public int catId;
    public int favorite;
    public int id;
    public String txtChinese;
    public String txtEnglish;
    public String txtKorean;
    public String txtPinyin;
    public String txtVietnamese;
    public String voice;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getTxtKorean() {
        return this.txtKorean;
    }

    public void setTxtKorean(String str) {
        this.txtKorean = str;
    }

    public String getTxtPinpyn() {
        return this.txtPinyin;
    }

    public void setTxtPinpyn(String str) {
        this.txtPinyin = str;
    }

    public String getTxtVietnamese() {
        return this.txtVietnamese;
    }

    public void setTxtVietnamese(String str) {
        this.txtVietnamese = str;
    }

    public int getCatId() {
        return this.catId;
    }

    public void setCatId(int i) {
        this.catId = i;
    }

    public String getVoice() {
        return this.voice;
    }

    public void setVoice(String str) {
        this.voice = str;
    }

    public int getFavorite() {
        return this.favorite;
    }

    public void setFavorite(int i) {
        this.favorite = i;
    }
}
