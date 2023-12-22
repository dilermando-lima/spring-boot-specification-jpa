package demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.config.FilterInterceptor.PublicEndpoint;
import demo.config.FilterInterceptor.RequiresProfile;
import demo.model.UserProfile;
import demo.service.AccountService;
import demo.service.AccountService.CreateRequestDTO;
import demo.service.AccountService.CreateResponseDTO;
import demo.service.AccountService.FindByIdResponseDTO;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PublicEndpoint
    @PostMapping
    public CreateResponseDTO create(@RequestBody CreateRequestDTO request){
        return accountService.create(request);
    }

    @RequiresProfile(UserProfile.READER)
    @GetMapping
    public FindByIdResponseDTO findById(){
        return accountService.findById();
    }

    @RequiresProfile(UserProfile.ADMINISTRATOR)
    @DeleteMapping
    public ResponseEntity<Void> remove(){
        accountService.remove();
        return ResponseEntity.noContent().build();
    }
    
}
