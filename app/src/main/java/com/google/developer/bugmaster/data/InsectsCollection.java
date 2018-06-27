package com.google.developer.bugmaster.data;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InsectsCollection {

    @SerializedName("insects")
    List<Insect> insects;
}
