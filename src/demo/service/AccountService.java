package demo.service;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.base.ContextAccount;
import demo.config.ExceptionRest;
import demo.model.Account;
import demo.repository.AccountRepository;


@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public record CreateRequestDTO(String name) {}
    public record CreateResponseDTO(String id, ZonedDateTime dateInsert, String name) {}
    public record FindByIdResponseDTO(String id, ZonedDateTime dateInsert, String name) {}

    @Transactional
    public CreateResponseDTO create(CreateRequestDTO request) {
        var account = new Account();
        account.setName(request.name());
        account = accountRepository.save(account);
        return new CreateResponseDTO(account.getId(), account.getDateInsert(), account.getName());
    }

    @Transactional
    public void remove(){
        var accountId = ContextAccount.accountId();
        ExceptionRest.throwNotFoundIF("account not found", !accountRepository.existsById(accountId));
        accountRepository.deleteById(accountId);
    }

    public FindByIdResponseDTO findById(){
        var account = accountRepository
                        .findById(ContextAccount.accountId())
                        .orElse(null);
                        
        account = ExceptionRest.throwNotFoundIFNull("account not found", account);

        return new FindByIdResponseDTO(account.getId(), account.getDateInsert(), account.getName());
    }
}
