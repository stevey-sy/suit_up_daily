package com.example.suitupdaily;

import com.google.gson.annotations.SerializedName;

public class ResponsePOJO {

    private boolean status;
    private String remarks;
    private String[] myLikeList;
    private String nick;
    private String birth;
    private String sex;
    private String photoUrl;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String[] getMyLikeList() {
        return myLikeList;
    }

    public void setMyLikeList(String[] myLikeList) {
        this.myLikeList = myLikeList;
    }

    @SerializedName("img_url")
    private String imgURL;

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @SerializedName("user_nickname")
    private String userNick;

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    @SerializedName("who_liked")
    private String whoLiked;

    public String getWhoLiked() {
        return whoLiked;
    }
    public void setWhoLiked(String whoLiked) {
        this.whoLiked = whoLiked;
    }

    // DB 에서 조회수 데이터 가져올 때 사용할 메소드 선언
    @SerializedName("view")
    private String view;

    public String getView() {
        return view;
    }

    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    @SerializedName("idx")
    private String idx;

    @SerializedName("user_id")
    private String id;

    @SerializedName("category")
    private String category;

    @SerializedName("img_location")
    private String picture;

    @SerializedName("date")
    private String date;

    @SerializedName("color")
    private String color;

    @SerializedName("season")
    private String season;

    @SerializedName("cloth_type")
    private String type;

    @SerializedName("fit")
    private String fit;

    @SerializedName("hash_tag")
    private String tags;

    @SerializedName("memo")
    private String memo;

    @SerializedName("like_num")
    private int like;

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFit() {
        return fit;
    }

    public void setFit(String fit) {
        this.fit = fit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStatus () {
        return status;
    }

    public String getRemarks () {
        return remarks;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
