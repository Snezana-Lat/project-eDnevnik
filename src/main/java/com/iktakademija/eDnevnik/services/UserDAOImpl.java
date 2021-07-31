package com.iktakademija.eDnevnik.services;

import org.springframework.stereotype.Service;

@Service
public class UserDAOImpl implements UserDAO{

	@Override
	public String makePassword(int length) {
		String password="";
		for(int i=0; i<length-2;i++) {
			password= password+ randomCharacter("abcdefghijklmnoppqrstuvwxyz");
			
		}
		String randomDigit=randomCharacter("0123456789");
		password=insertAtRandom(password, randomDigit);
		String randomCharacter=randomCharacter("ABCDEFGGHIJKLMNOPQRSTUVWXYZ");
		password=insertAtRandom(password, randomCharacter);
		
		return password;
	}

	@Override
	public String randomCharacter(String characters) {
		int n= characters.length();
		int r=(int)(n*Math.random());
		return characters.substring(r, r+1);
	}

	@Override
	public String insertAtRandom(String s, String toInsert) {
		int n=s.length();
		int r=(int)((n+1)*Math.random());
		
		return s.subSequence(0, r)+toInsert+s.substring(r);
	}

}
