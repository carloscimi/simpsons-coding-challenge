package com.example.simpsonschallengeapi.model.generateID;

import java.util.ArrayList;
import java.util.List;

public class GenerateID {
	
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toLowerCase();
	
	private static List<String> generatedIDs = new ArrayList<String>();

	public static String generateID() {
		
		int count = 24;
		
		StringBuilder builder = new StringBuilder();
		
		while (count-- != 0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		
		String generatedID = builder.toString();
		
		if(generatedIDs.contains(generatedID)) {
			generateID();
		} else {
			generatedIDs.add(generatedID);
		}
		
		return generatedID;
	}
}
