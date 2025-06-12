package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames="username")})
public class UserEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id; //유저에게 고유하게 부여되는 id

    @Column(nullable=false)
    private String username; // 아이디로 사용할 유저네임(이메일 or 문자열)
    private String password;
    private String role; // 어디민/일반사용자
    private String authProvider; // 이후 OAuth에서 사용할 유저 정보 제공자 : github
}
