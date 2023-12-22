package demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.config.FilterInterceptor.RequiresProfile;
import demo.model.UserProfile;
import demo.service.CompanyService;
import demo.service.CompanyService.AlterRequestDTO;
import demo.service.CompanyService.CreateRequestDTO;
import demo.service.CompanyService.CreateResponseDTO;
import demo.service.CompanyService.FindByIdResponseDTO;
import demo.service.CompanyService.ListItemResponseDTO;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @RequiresProfile(UserProfile.WRITER)
    @PostMapping
    public CreateResponseDTO create(@RequestBody CreateRequestDTO request){
        return companyService.create(request);
    }

    @RequiresProfile(UserProfile.WRITER)
    @PutMapping("/{id}")
    public ResponseEntity<Void> alter(@PathVariable(name = "id") String id, @RequestBody AlterRequestDTO request){
        companyService.alter(id, request);
        return ResponseEntity.noContent().build();
    }

    @RequiresProfile(UserProfile.READER)
    @GetMapping("/{id}")
    public FindByIdResponseDTO findById(@PathVariable(name = "id") String id){
        return companyService.findById(id);
    }

    @RequiresProfile(UserProfile.READER)
    @GetMapping
    public List<ListItemResponseDTO> list(){
        return companyService.list();
    }

    @RequiresProfile(UserProfile.OWNER)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable(name = "id") String id){
        companyService.remove(id);
        return ResponseEntity.noContent().build();
    }
    
}
