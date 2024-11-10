package com.hollysgang.sample.distributedLock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(String name, BigDecimal balance) {
        Account account = new Account(name, balance);
        return accountRepository.save(account);
    }


    public BigDecimal getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("해당 ID의 계좌가 존재 하지 않습니다."));
        return account.getBalance();
    }

    public boolean withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            return false;
        }
        account.withdraw(amount);

        return true;
    }

    public boolean deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            return false;
        }
        account.deposit(amount);

        return true;
    }




}


