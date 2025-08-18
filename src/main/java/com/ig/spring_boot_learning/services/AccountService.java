package com.ig.spring_boot_learning.services;

import com.ig.spring_boot_learning.dto.AccountDto;
import com.ig.spring_boot_learning.model.Account;
import org.springframework.data.domain.Page;

public interface AccountService {
    Account createAccount(Account account);
    Account updateAccount(Long id, AccountDto accountDto);
    Account getAccountDetail(Long id);
    Page<Account> getAccountList(String query, int page, int size);
    void deleteAccount(Long id);
}
