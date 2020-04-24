package com.example.simpsonschallengeapi.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.example.simpsonschallengeapi.model.Data;
import com.example.simpsonschallengeapi.model.SimpsonsCharacter;
import com.example.simpsonschallengeapi.model.SimpsonsPhrase;
import com.google.gson.Gson;

@Component
@ConfigurationProperties("db")
public class DataBase {

	@Autowired
	Gson gson;
	
	@Value("${db.characters}")
	private String dbCharactersPath;
	
	@Value("${db.phrases}")
	private String dbPhrasesPath;
	
	protected static Map<Class<?>, Data<?>> database = new HashMap<Class<?>, Data<?>>();	
	
	public <T> ArrayList<T> getData(Class<T> c) {
		
		if(!database.containsKey(SimpsonsCharacter.class)) {
			Data<SimpsonsCharacter> charactersData = getFile(dbCharactersPath);
			database.put(SimpsonsCharacter.class, charactersData);
		}
		
		if(!database.containsKey(SimpsonsPhrase.class)) {
			Data<SimpsonsPhrase> phrasesData = getFile(dbPhrasesPath);
			database.put(SimpsonsPhrase.class, phrasesData);
		}
		
		Data<?> data = database.get(c);
		
		ArrayList<T> list = new ArrayList<>();
		
		for (int i = 0; i < data.getData().size(); i++) {
			String str = gson.toJson(data.getData().get(i));
			T t = gson.fromJson(str, c);
			list.add(t);
		}
			
		return list;
	}

	public <T> void persistData(ArrayList<T> listUpdated, Class<T> Class) {
		
		Data<T> data = new Data<>();
		data.setData(listUpdated);
		
		DataBase.database.put(Class, data);
	}
	
	
	@SuppressWarnings("unchecked")
	protected <T> Data<T> getFile(String filePath) {
		
		ClassPathResource classPathResource = new ClassPathResource(filePath);
		
		File file;
		
		String result = "";
		
		try {
			file = classPathResource.getFile();
			result = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return gson.fromJson(result, Data.class);
	}


	public static Map<Class<?>, Data<?>> getDatabase() {
		return database;
	}
	
}
