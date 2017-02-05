package com.example.rfsh.rsauveho_sizebook;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rfsh on 2017-01-27.
 */

public class Record implements Serializable{
    private String name;
    private Date date;
    private int neck;
    private int bust;
    private int chest;
    private int waist;
    private int hip;
    private int inseam;
    private String comment;

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public int getNeck() {
        return neck;
    }

    public int getBust() {
        return bust;
    }

    public int getChest() {
        return chest;
    }

    public int getWaist() {
        return waist;
    }

    public int getHip() {
        return hip;
    }

    public int getInseam() {
        return inseam;
    }

    public String getComment() {
        return comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNeck(int neck) {
        this.neck = neck;
    }

    public void setBust(int bust) {
        this.bust = bust;
    }

    public void setChest(int chest) {
        this.chest = chest;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }

    public void setHip(int hip) {
        this.hip = hip;
    }

    public void setInseam(int inseam) {
        this.inseam = inseam;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Record(String name) {
        this.name = name;
    }

    public Record(String name, Date date, int neck, int bust, int chest, int waist,
                  int hip, int inseam, String comment){
        this.name = name;
        this.date = date != null ? date : new Date();
        this.neck = neck;
        this.bust = bust;
        this.chest = chest;
        this.waist = waist;
        this.hip = hip;
        this.inseam = inseam;
        this.comment = comment != null ? comment : new String();
    }

    public String toString() {
        return  "Name: " + name + "\n" +
                (bust < 0 ? "" : ("Bust: " + bust + "\n")) +
                (chest < 0 ? "" : ("Chest: " + chest + "\n)")) +
                (waist < 0 ? "" : ("Waist: " + waist + "\n")) +
                (inseam < 0 ? "" : ("Inseam: " + inseam));
    }
}
