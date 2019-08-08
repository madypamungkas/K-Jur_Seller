package com.komsi.lab.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class Location {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("nama")
    @Expose
    private String nama;

    public Location(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }
}
