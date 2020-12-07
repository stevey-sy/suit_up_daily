package com.example.suitupdaily.filter;

import java.io.Serializable;

public class FilterSeason implements Serializable {

    private static final long serialVersionUID = 1L;
    private String season;
    private boolean isSelected;

    public FilterSeason () {

    }

    public FilterSeason (String season) {
        this.season = season;
        this.isSelected = isSelected;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
