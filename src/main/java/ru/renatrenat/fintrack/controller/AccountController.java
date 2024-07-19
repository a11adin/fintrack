package ru.renatrenat.fintrack.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.renatrenat.fintrack.filter.AccountFilter;
import ru.renatrenat.fintrack.model.Account;
import ru.renatrenat.fintrack.repository.AccountRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
//TODO Не работает, написать всю свою логику
    private final AccountRepository accountRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public Page<Account> getList(@ModelAttribute AccountFilter filter, @ParameterObject Pageable pageable) {
        Specification<Account> spec = filter.toSpecification();
        return accountRepository.findAll(spec, pageable);
    }

    @GetMapping("/{id}")
    public Account getOne(@PathVariable Long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        return accountOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Account> getMany(@RequestParam List<Long> ids) {
        return accountRepository.findAllById(ids);
    }

    @PostMapping
    public Account create(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    @PatchMapping("/{id}")
    public Account patch(@PathVariable long id, @RequestBody JsonNode patchNode) throws IOException {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(account).readValue(patchNode);

        return accountRepository.save(account);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Account> accounts = accountRepository.findAllById(ids);

        for (Account account : accounts) {
            objectMapper.readerForUpdating(account).readValue(patchNode);
        }

        List<Account> resultAccounts = accountRepository.saveAll(accounts);
        List<Long> ids1 = resultAccounts.stream()
                .map(Account::getId)
                .toList();
        return ids1;
    }

    @DeleteMapping("/{id}")
    public Account delete(@PathVariable long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            accountRepository.delete(account);
        }
        return account;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        accountRepository.deleteAllById(ids);
    }
}
