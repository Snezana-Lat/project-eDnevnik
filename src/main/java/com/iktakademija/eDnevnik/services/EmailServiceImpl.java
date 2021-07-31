package com.iktakademija.eDnevnik.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.iktakademija.eDnevnik.entities.OcenaEntity;

@Service
public class EmailServiceImpl implements EmailService{



	@Autowired
	public JavaMailSender emailSender;

	@Override
	public void posaljiMailRoditelju(OcenaEntity ocena) throws MessagingException {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(ocena.getUcenik().getRoditelj().getEmail());
		helper.setSubject("Nova ocena "+ocena.getUcenik().getName()+ " "+ ocena.getUcenik().getLastname());
		String text = "<html><style>\r\n"
				+ "table, th, td {\r\n"
				+ "  border: 1px solid black;\r\n"
				+ "}\r\n"
				+ "</style><body><table >\r\n"
				+ "<tr>\r\n"
				+ "<th>Ucenik</th>\r\n"
				+ "<th>Predmet</th>\r\n"
				+ "<th>Ocena</th>\r\n"
				+ "<th>Datum</th>\r\n"
				+ "<th>Nastavnik</th>\r\n"
				+ "</tr>\r\n"
				+ "<tr>\r\n"
				+ "<td>"+ocena.getUcenik().getName()+" "+ocena.getUcenik().getLastname()+"</td>\r\n"
				+ "<td>"+ocena.getPredmet().getNaziv()+"</td>\r\n"
				+ "<td>"+ocena.getOcena()+"</td>\r\n"
				+ "<td>"+ocena.getDatumOcenjivanja()+"</td>\r\n"
				+ "<td>"+ocena.getNastavnik().getName()+" "+ocena.getNastavnik().getLastname()+"</td>\r\n"
				+ "</tr>\r\n"
				+ "</table></body></html>";
		helper.setText(text, true);
		emailSender.send(mail);
	}
}
