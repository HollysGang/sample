package com.hollysgang.sample.distributedLock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @DistributedLock(key = "#name")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account createAccount(String name, Long balance) {
        Account account = new Account(name, balance);
        return accountRepository.save(account);
    }


    public Long getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("해당 ID의 계좌가 존재 하지 않습니다."));
        return account.getBalance();
    }

    @DistributedLock(key = "#accountId")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean withdraw(Long accountId, Long amount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            return false;
        }
        account.withdraw(amount);

        return true;
    }

    @DistributedLock(key = "#accountId")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deposit(Long accountId, Long amount) throws InterruptedException {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            return false;
        }
        account.deposit(amount);
        Thread.sleep(1000);
        return true;
    }




}


