package com.ig.spring_boot_learning.dto;

import com.ig.spring_boot_learning.model.Account;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    @NotBlank
    private String accountHolderName;
    @NotBlank
    private String accountNumber;
    @NotBlank
    private String accountType;
    private BigDecimal balance;

    public Account toAccount() {
        return new Account(id, accountHolderName, accountNumber, accountType, balance);
    }

    public Account updateAccount(Account account) {
        account.setAccountHolderName(accountHolderName);
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setBalance(balance);
        return account;
    }
}
