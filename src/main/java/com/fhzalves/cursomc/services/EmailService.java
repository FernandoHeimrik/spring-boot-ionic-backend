package com.fhzalves.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.fhzalves.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
		
	void sendEmail(SimpleMailMessage msg);

}
