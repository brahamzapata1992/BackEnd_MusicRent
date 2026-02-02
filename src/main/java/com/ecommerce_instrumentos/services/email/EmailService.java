package com.ecommerce_instrumentos.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendRegistrationSuccessEmail(String to, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Registro Exitoso");
        message.setText("¡Hola" + userName +
                "\n" +
                "Tu registro en MusicRent ha sido exitoso. Estamos emocionados de darte la bienvenida a nuestra comunidad de amantes de la música.\n" +
                "Nombre de usuario: [Nombre de usuario]\n" +
                "Correo electrónico: [Correo electrónico]\n" +
                "\n" +
                "Para comenzar a explorar nuestra amplia selección de instrumentos musicales disponibles para rentar al mejor precio, haz clic en el botón iniciar sesión.");

        javaMailSender.send(message);
    }
}


