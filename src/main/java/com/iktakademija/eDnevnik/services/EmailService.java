package com.iktakademija.eDnevnik.services;

import javax.mail.MessagingException;

import com.iktakademija.eDnevnik.entities.OcenaEntity;


public interface EmailService {
	public void posaljiMailRoditelju(OcenaEntity ocena)throws MessagingException;
}
