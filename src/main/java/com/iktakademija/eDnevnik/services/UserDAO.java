package com.iktakademija.eDnevnik.services;

public interface UserDAO {

	public String makePassword(int lenght);
	public String randomCharacter(String characters);
	public String insertAtRandom(String s, String toInsert);
	
	
}
