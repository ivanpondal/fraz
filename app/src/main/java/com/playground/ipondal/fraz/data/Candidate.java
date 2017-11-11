package com.playground.ipondal.fraz.data;

import java.util.HashMap;
import java.util.Map;

public class Candidate {
	private final String id;
	private final String character;
	private final String imageRef;

	public Candidate(String id, String character, String imageRef) {
		this.id = id;
		this.character = character;
		this.imageRef = imageRef;
	}

	public static Candidate withIdWithCharacterNameAndImageRef(String id, String character, String imageRef) {
		return new Candidate(id, character, imageRef);
	}

	public String id() {
		return id;
	}

	public String character() {
		return character;
	}

	public String imageRef() {
		return imageRef;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> result = new HashMap<>();
		result.put("uid", id);
		result.put("character-name", character);
		result.put("image-ref", imageRef);
		return result;
	}
}
