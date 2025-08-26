package com.ig.spring_boot_learning.controllers;

import com.ig.spring_boot_learning.annotations.logging.AuditFilter;
import com.ig.spring_boot_learning.base.response.ObjectResponse;
import com.ig.spring_boot_learning.base.response.PageResponse;
import com.ig.spring_boot_learning.base.response.ResponseMessage;
import com.ig.spring_boot_learning.dto.AccountDto;
import com.ig.spring_boot_learning.model.Account;
import com.ig.spring_boot_learning.services.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.ig.spring_boot_learning.utils.AppConstants.*;

@RestController
@RequestMapping(API_PREFIX + "/account")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ObjectResponse<Account> getAccount(@Valid @RequestBody AccountDto accountDto) {
        return new ObjectResponse<>(accountService.createAccount(accountDto.toAccount()));
    }

    @PutMapping(ID_PATH)
    public ObjectResponse<Account> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountDto account) {
        return new ObjectResponse<>(accountService.updateAccount(id, account));
    }

    @GetMapping(ID_PATH)
    public ObjectResponse<AccountDto> getAccountById(@PathVariable Long id) {
        return new ObjectResponse<>(accountService.getAccountDetail(id).toAccountDto());
    }

    @GetMapping(LIST)
    @AuditFilter
    public PageResponse<AccountDto> getAccountListPage(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = MAX_PAGE) int page,
            @RequestParam(defaultValue = MAX_SIZE) int size
    ) {
        var listPage = accountService.getAccountList(query, page, size);
        var accountDtoList = listPage.getContent().stream().map(Account::toAccountDto).toList();
        return new PageResponse<>(accountDtoList, listPage.getTotalElements());
    }

    @DeleteMapping(ID_PATH)
    public ResponseMessage deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return new ResponseMessage();
    }
}
