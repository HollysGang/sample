package com.hollysgang.sample.distributedLock;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false)
    private Long balance;

    private LocalDateTime createdDate;

    @Builder
    public Account(String name, Long balance) {
        this.name = name;
        this.balance = balance;
        this.createdDate = LocalDateTime.now();
    }

    // 입금 메서드
    public void deposit(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("입금 요청 금액이 0보다 작습니다.");
        }
        this.balance = this.balance+amount;
    }

    // 출금 메서드
    public void withdraw(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("출금 요청 금액이 0보다 작습니다.");
        }
        if (this.balance - amount < 0) {
            throw new IllegalArgumentException("잔액이 충분하지 않습니다.");
        }
        this.balance = this.balance- amount;
    }
}
