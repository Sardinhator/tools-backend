package com.intelcom.hora_tools.hora_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class HoraToolsApplication {

	private static final Logger log = LoggerFactory.getLogger(HoraToolsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(HoraToolsApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		log.info("✨ HoraTools Application is ready and running!");
	}

}
