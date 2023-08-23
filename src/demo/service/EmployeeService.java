package demo.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.model.Company;
import demo.model.Employee;
import demo.repository.CompanyRepository;
import demo.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyRepository companyRepository;

    public record CreateRequestDTO(String name, String idCompany) {}
    public record CreateResponseDTO(String id, ZonedDateTime dateInsert, String name) {}
    public record AlterRequestDTO(String name, String idCompany) {}
    public record FindByIdResponseDTO(String id, String idCompany, String nameCompany, ZonedDateTime dateInsert, String name) {}
    public record ListItemResponseDTO(String id, String idCompany, String nameCompany, ZonedDateTime dateInsert, String name) {}

    @Transactional
    public CreateResponseDTO create(CreateRequestDTO request) {

        var company = new Company();
        company.setId(request.idCompany());
        var employee = new Employee();
        employee.setCompany(company);
        employee.setName(request.name());

        employee = employeeRepository.saveOnContext(employee);
        return new CreateResponseDTO(employee.getId(), employee.getDateInsert(), employee.getName());
    }

    @Transactional
    public void alter(String id, AlterRequestDTO request) {
        var employee = new Employee();
        var company = companyRepository.findByIdOnContext(request.idCompany());
        employee.setCompany(company);
        employee.setName(request.name());
        employeeRepository.saveOnContext(employee);
    }

    public List<ListItemResponseDTO> list() {
        return employeeRepository
                .listOnContext()
                .stream()
                .map(c -> new ListItemResponseDTO(c.getId(), c.getCompany().getId(), c.getCompany().getName(), c.getDateInsert(), c.getName()))
                .toList();
    }

    @Transactional
    public void remove(String id) {
        employeeRepository.deleteById(id);
    }

    public FindByIdResponseDTO findById(String id){
        var employee = employeeRepository.findByIdOnContext(id);
        return new FindByIdResponseDTO(
            employee.getId(), 
            employee.getCompany().getId(), 
            employee.getCompany().getName(), 
            employee.getDateInsert(), 
            employee.getName()
        );
    }
}
