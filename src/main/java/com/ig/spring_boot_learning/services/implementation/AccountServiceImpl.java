package com.ig.spring_boot_learning.services.implementation;

import com.ig.spring_boot_learning.dto.AccountDto;
import com.ig.spring_boot_learning.exceptions.ApiErrorException;
import com.ig.spring_boot_learning.model.Account;
import com.ig.spring_boot_learning.repositories.AccountRepository;
import com.ig.spring_boot_learning.services.AccountService;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Long id, AccountDto accountDto) {
        Account account = getAccountDetail(id);
        Account updatedAccount = accountDto.updateAccount(account);
        return accountRepository.save(updatedAccount);
    }

    @Override
    public Account getAccountDetail(Long id) {
        return accountRepository.findByIdAndStatusTrue(id).orElseThrow(() -> new ApiErrorException(404, "Account not found"));
    }

    @Override
    public Page<Account> getAccountList(String query, int page, int size) {
        return accountRepository.findAll((root, cq, cb) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();

            if (query != null) {

                var search = "%" + query.toUpperCase() + "%";

                var accountName = cb.like(cb.upper(root.get("accountHolderName")), search);
                var accountNum = cb.like(cb.upper(root.get("accountNumber")), search);
                var accountType = cb.like(cb.upper(root.get("accountType")), search);
                predicates.add(cb.or(accountName, accountNum, accountType));
            }
            Objects.requireNonNull(cq).orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(page, size));
    }

    @Transactional
    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
