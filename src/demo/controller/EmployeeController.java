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

import demo.service.EmployeeService;
import demo.service.EmployeeService.AlterRequestDTO;
import demo.service.EmployeeService.CreateRequestDTO;
import demo.service.EmployeeService.CreateResponseDTO;
import demo.service.EmployeeService.FindByIdResponseDTO;
import demo.service.EmployeeService.ListItemResponseDTO;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService companyService;

    @PostMapping
    public CreateResponseDTO create(@RequestBody CreateRequestDTO request){
        return companyService.create(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alter(@PathVariable(name = "id") String id, @RequestBody AlterRequestDTO request){
        companyService.alter(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public FindByIdResponseDTO findById(@PathVariable(name = "id") String id){
        return companyService.findById(id);
    }

    @GetMapping
    public List<ListItemResponseDTO> list(){
        return companyService.list();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable(name = "id") String id){
        companyService.remove(id);
        return ResponseEntity.noContent().build();
    }
    
}
