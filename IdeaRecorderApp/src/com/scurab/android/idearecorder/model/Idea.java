package com.scurab.android.idearecorder.model;

import java.util.Date;

public class Idea implements Comparable<Idea> {

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_AUDIO = 3;
    public static final int TYPE_VIDEO = 4;

    private long id;
    private int ideaType;
    private String name;
    private String description;
    private String path;
    private long savetime;

    public long getId() {
	return id;
    }

    public Idea setId(long id) {
	this.id = id;
	return this;
    }

    public String getName() {
	return name;
    }

    public Idea setName(String name) {
	this.name = name;
	return this;
    }

    public String getDescription() {
	return description;
    }

    public Idea setDescription(String description) {
	this.description = description;
	return this;
    }

    public String getPath() {
	return path;
    }

    public Idea setPath(String path) {
	this.path = path;
	return this;
    }

    public long getSaveTime() {
	return savetime;
    }

    public Idea setSaveTime(long savetime) {
	this.savetime = savetime;
	return this;
    }

    public int getIdeaType() {
	return ideaType;
    }

    public Idea setIdeaType(int ideaType) {
	this.ideaType = ideaType;
	return this;
    }

    @Override
    public int compareTo(Idea another) {
	return ((Long) another.id).compareTo(id);
    }

    @Override
    public String toString() {
	return String.format("%s:%s (%s)[%s]", getId(), getName(), new Date(
		getSaveTime()).toLocaleString(), getIdeaType());
    }
}
