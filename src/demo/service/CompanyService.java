package demo.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.model.Company;
import demo.repository.CompanyRepository;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public record CreateRequestDTO(String name) {}
    public record CreateResponseDTO(String id, ZonedDateTime dateInsert, String name) {}
    public record FindByIdResponseDTO(String id, ZonedDateTime dateInsert, String name) {}
    public record AlterRequestDTO(String name) {}
    public record ListItemResponseDTO(String id, ZonedDateTime dateInsert, String name) {}

    @Transactional
    public CreateResponseDTO create(CreateRequestDTO request) {
        var company = new Company();
        company.setName(request.name());
        company = companyRepository.saveOnContext(company);
        return new CreateResponseDTO(company.getId(), company.getDateInsert(), company.getName());
    }

    @Transactional
    public void alter(String id, AlterRequestDTO request) {
        var company = new Company();
        company.setName(request.name());
        company.setId(id);
        companyRepository.saveOnContext(company);
    }

    public List<ListItemResponseDTO> list() {
        return companyRepository
                .listOnContext()
                .stream()
                .map(c -> new ListItemResponseDTO(c.getId(), c.getDateInsert(), c.getAccount().getName()))
                .toList();
    }

    @Transactional
    public void remove(String id){
        companyRepository.deleteByIdOnContext(id);
    }

    public FindByIdResponseDTO findById(String id){
        var company = companyRepository.findByIdOnContext(id);
        return new FindByIdResponseDTO(company.getId(), company.getDateInsert(), company.getName());
    }
}
