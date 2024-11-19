package com.hollysgang.sample.distributedLock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        int threadCount = 10; // 동시에 실행할 스레드 수
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Integer> executionOrder = Collections.synchronizedList(new ArrayList<>());
        // 각 스레드에서 deposit과 withdraw를 동시에 호출
        for (int i = 0; i < threadCount; i++) {
            int threadId = i;
            Thread.sleep(40);
            executorService.execute(() -> {
                try {
                    accountService.deposit(accountId, 100L);
                    executionOrder.add(threadId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                latch.countDown();
            });

        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();
        List<Integer> expectedOrder = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            expectedOrder.add(i);
        }
        assertEquals(expectedOrder, executionOrder, "요청 순서와 처리 순서가 일치하지 않습니다.");


        // 최종 잔액을 확인하여 동시성 문제가 발생하지 않았는지 확인
        Account account = accountRepository.findById(accountId).orElseThrow();
        Long expectedBalance = 2000L;
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