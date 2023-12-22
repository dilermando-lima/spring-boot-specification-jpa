package demo.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.config.ExceptionRest;
import demo.config.SessionRequest;
import demo.model.Company;
import demo.repository.CompanyRepository;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    SessionRequest sessionRequest;

    public record CreateRequestDTO(String name) {}
    public record CreateResponseDTO(String id, ZonedDateTime dateInsert, String name) {}
    public record FindByIdResponseDTO(String id, ZonedDateTime dateInsert, String name) {}
    public record AlterRequestDTO(String name) {}
    public record ListItemResponseDTO(String id, ZonedDateTime dateInsert, String name) {}

    @Transactional
    public CreateResponseDTO create(CreateRequestDTO request) {
        var company = new Company();
        company.setName(request.name());
        company = companyRepository.save(company);
        return new CreateResponseDTO(company.getId(), company.getDateInsert(), company.getName());
    }

    @Transactional
    public void alter(String id, AlterRequestDTO request) {
        var company = new Company();
        company.setName(request.name());
        company.setId(id);
        companyRepository.save(company);
    }

    public List<ListItemResponseDTO> list() {

        companyRepository.count();

        return companyRepository
                .listOnAccountContext(null, sessionRequest.getAccountId())
                .stream()
                .map(c -> new ListItemResponseDTO(c.getId(), c.getDateInsert(), c.getAccount().getName()))
                .toList();
    }

    @Transactional
    public void remove(String id){
        ExceptionRest.throwNotFoundIF("company not found", id == null || id.trim().isBlank() || !companyRepository.existsByIdOnAccountContext(id, sessionRequest.getAccountId()));
        companyRepository.deleteByIdOnAccountContext(id, sessionRequest.getAccountId());
    }

    public FindByIdResponseDTO findById(String id){
        ExceptionRest.throwNotFoundIF("company not found", id == null || id.trim().isBlank() || !companyRepository.existsByIdOnAccountContext(id, sessionRequest.getAccountId()));
        var company = companyRepository.findByIdOnAccountContext(id, sessionRequest.getAccountId());
        return new FindByIdResponseDTO(company.getId(), company.getDateInsert(), company.getName());
    }
}
