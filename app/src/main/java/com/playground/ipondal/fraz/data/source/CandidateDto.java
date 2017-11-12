package com.playground.ipondal.fraz.data.source;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

public class CandidateDto {
	public String uid;
	@PropertyName("character-name")
	public String characterName;
	@PropertyName("image-ref")
	public String imageRef;

	public CandidateDto() {
	}
}
