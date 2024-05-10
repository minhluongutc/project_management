package datn.backend;

import datn.backend.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ProjectManagementApplication {
	@Autowired
	EmailSenderService senderService;

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementApplication.class, args);
	}
//	@EventListener(ApplicationReadyEvent.class)
//	public void sendMail() {
//		senderService.sendEmail("nguyenminhluong.work@gmail.com", "This is Subject", "This is Body");
//	}

}
