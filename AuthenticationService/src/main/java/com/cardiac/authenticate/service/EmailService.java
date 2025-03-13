package com.cardiac.authenticate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	private final JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	private String senderEmail;

	@Autowired
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendEmail(String to, String subject, String body) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			helper.setFrom("suddalaganesh1239@gamil.com");
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException("Error while sending email: " + e.getMessage());
		}
	}

//	public void sendTemporaryPassword(String toEmail, String tempPassword) {
//		try {
//			MimeMessage message = mailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//			helper.setFrom(senderEmail);
//			helper.setTo(toEmail);
//			helper.setSubject("Your Temporary Password");
//			helper.setText("Your temporary password is: " + tempPassword);
//			mailSender.send(message);
//			System.out.println("Temporary password sent to email: " + toEmail);
//		} catch (Exception e) {
//			e.printStackTrace();
//			// Handle error while sending email
//		}
//	}

	// New method for Forgot Password functionality
//	public void sendForgotPasswordEmail(String username, String email) {
//		try {
//			MimeMessage message = mailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//			helper.setFrom(senderEmail);
//			helper.setTo(email);
//			helper.setSubject("Password Reset Request");
//			helper.setText("Hello " + username + ",\n\n"
//					+ "We received a request to reset your password. Please follow the instructions to reset it.\n\n"
//					+ "If you did not request a password reset, please ignore this email.\n\n" + "Thank you!");
//			mailSender.send(message);
//			System.out.println("Password reset email sent to: " + email);
//		} catch (Exception e) {
//			e.printStackTrace();
//			// Handle error while sending email
//		}
//	}
}
