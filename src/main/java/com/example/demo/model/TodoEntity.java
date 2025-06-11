package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder //= 오브젝트 생성(생성자 대체)
@NoArgsConstructor
@AllArgsConstructor
@Data // 맴버변수의 getter, setter 메서드 구현
@Entity
@Table(name="Todo")
public class TodoEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    private String id;  // 이 오브젝트의 아이디
    private String userId; // 오브젝트 생성 유저 id
    private String title; // 운동하기
    private boolean done; // 완료한경우 true
}
