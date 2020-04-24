package com.example.simpsonschallengeapi.model;

import java.util.ArrayList;

public class Data<T> {

	ArrayList<T> data = new ArrayList<T>();

	public ArrayList<T> getData() {
		return data;
	}

	public void setData(ArrayList<T> data) {
		this.data = data;
	}

}
