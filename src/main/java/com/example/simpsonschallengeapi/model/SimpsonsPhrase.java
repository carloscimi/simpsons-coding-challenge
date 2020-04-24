package com.example.simpsonschallengeapi.model;

import javax.validation.constraints.NotNull;

public class SimpsonsPhrase {

	private String _id;
	
	@NotNull
	private String character;
	
	private String phrase;
	
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String getCharacter() {
		return character;
	}
	public void setCharacter(String character) {
		this.character = character;
	}
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpsonsPhrase other = (SimpsonsPhrase) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
	
}
