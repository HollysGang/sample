package com.hollysgang.sample.distributedLock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")  // H2 데이터베이스를 사용하도록 테스트 프로파일 설정
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    private Long accountId;

    @BeforeEach
    void setUp() {
        // 초기 잔액이 1000인 계좌를 생성
        Account account = new Account("John Doe", 1000L);
        accountRepository.save(account);
        accountId = account.getId();
    }

    @Test()
    @DisplayName("동시성 확인")
    void testConcurrentDepositAndWithdraw() throws InterruptedException {
        int threadCount = 5; // 동시에 실행할 스레드 수
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 각 스레드에서 deposit과 withdraw를 동시에 호출
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                accountService.deposit(accountId, 100L);
                latch.countDown();
            });

//            executorService.execute(() -> {
//                accountService.withdraw(accountId, 100L);
//                latch.countDown();
//            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        // 최종 잔액을 확인하여 동시성 문제가 발생하지 않았는지 확인
        Account account = accountRepository.findById(accountId).orElseThrow();
        Long expectedBalance = 1000L; // 초기 잔액
        assertEquals(expectedBalance, account.getBalance(), "동시성 문제로 인해 잔액이 일치하지 않습니다.");
    }

//    @Test
//    void createAccount() {
//    }
//
//    @Test
//    void getBalance() {
//    }
//
//    @Test
//    void withdraw() {
//    }
//
//    @Test
//    void deposit() {
//    }
}