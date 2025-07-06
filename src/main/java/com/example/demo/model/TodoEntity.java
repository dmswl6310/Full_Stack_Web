package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Todo")
public class TodoEntity {

    @Id
    private String id;  // 이 오브젝트의 아이디

    private String userId; // 오브젝트 생성 유저 id
    private String title;  // 운동하기
    private boolean done;  // 완료한 경우 true

    // ✅ 저장되기 전에 UUID를 String으로 생성해서 id에 넣어줌
    @PrePersist
    public void assignId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}