package com.ig.spring_boot_learning.model;

import com.ig.spring_boot_learning.base.model.BaseEntity;
import com.ig.spring_boot_learning.dto.AccountDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountHolderName;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;

    public AccountDto toAccountDto() {
        return new AccountDto(id, accountHolderName, accountNumber, accountType, balance);
    }
}
