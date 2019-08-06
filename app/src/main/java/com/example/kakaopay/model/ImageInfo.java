package com.example.kakaopay.model;

import com.example.kakaopay.model.Image_material.Documents;
import com.example.kakaopay.model.Image_material.Meta;

import java.util.ArrayList;

public class ImageInfo {

    private ArrayList<Documents>documents;
    private Meta meta;

    public ArrayList<Documents> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<Documents> documents) {
        this.documents = documents;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
