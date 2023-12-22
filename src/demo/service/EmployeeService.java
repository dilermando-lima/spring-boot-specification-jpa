package demo.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.config.SessionRequest;
import demo.filter.FilterParam;
import demo.filter.FilterBuilder;
import demo.filter.TypeFilter;
import demo.model.Company;
import demo.model.Employee;
import demo.repository.CompanyRepository;
import demo.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    SessionRequest sessionRequest;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyRepository companyRepository;

    public record CreateRequestDTO(String name, String idCompany) {}
    public record CreateResponseDTO(String id, ZonedDateTime dateInsert, String name) {}
    public record AlterRequestDTO(String name, String idCompany) {}
    public record FindByIdResponseDTO(String id, String idCompany, String nameCompany, ZonedDateTime dateInsert, String name) {}
    public record ListItemResponseDTO(String id, String idCompany, String nameCompany, ZonedDateTime dateInsert, String name) {}

    public record ListRequestDTO(

        @FilterParam(param = "name",type = TypeFilter.EQUALS)
        String name,

        @FilterParam(param = "company.name",type = TypeFilter.CONTAINS, requiresSufix = false)
        String companyName,

        @FilterParam(param = "account.id",type = TypeFilter.EQUALS)
        Integer accountId,

        @FilterParam(param = "test.date", ignoresOnQuery = true)
        LocalDate testeDate

    ) {}


    @Transactional
    public CreateResponseDTO create(CreateRequestDTO request) {

        var company = new Company();
        company.setId(request.idCompany());
        var employee = new Employee();
        employee.setCompany(company);
        employee.setName(request.name());

        employee = employeeRepository.save(employee);
        return new CreateResponseDTO(employee.getId(), employee.getDateInsert(), employee.getName());
    }

    @Transactional
    public void alter(String id, AlterRequestDTO request) {
        var employee = new Employee();
        var company = companyRepository.findByIdOnAccountContext(id, sessionRequest.getAccountId());
        employee.setCompany(company);
        employee.setName(request.name());
        employeeRepository.saveOnAccountContext(employee, sessionRequest.getAccountId());
    }

    public List<ListItemResponseDTO> list(ListRequestDTO listRequest) {

        return employeeRepository
                .findAll(FilterBuilder.<Employee>init(listRequest).build())
                .stream()
                .map(c -> new ListItemResponseDTO(c.getId(), c.getCompany().getId(), c.getCompany().getName(), c.getDateInsert(), c.getName()))
                .toList();
    }

    @Transactional
    public void remove(String id) {
        employeeRepository.deleteByIdOnAccountContext(id, sessionRequest.getAccountId());
    }

    public FindByIdResponseDTO findById(String id){
        var employee = employeeRepository.findByIdOnAccountContext(id, sessionRequest.getAccountId());
        return new FindByIdResponseDTO(
            employee.getId(), 
            employee.getCompany().getId(), 
            employee.getCompany().getName(), 
            employee.getDateInsert(), 
            employee.getName()
        );
    }
}
