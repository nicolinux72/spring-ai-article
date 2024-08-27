package it.nicolasanti.web;

import it.nicolasanti.manager.CompletionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AIController {

	@Autowired CompletionManager completionService;

	@GetMapping("/ai")
	String completion(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
		return completionService.chatResponse(message);
	}
}
