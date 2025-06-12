package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.UserEntity;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
        try{
            if(userDTO==null||userDTO.getPassword()==null){
                throw new RuntimeException("Invalid Password value.");
            }

            // 저장할 유저 만들기
            UserEntity user=UserEntity.builder()
                    .username(userDTO.getUsername())
                    .password(userDTO.getPassword())
                    .build();

            // 레포지토리에 유저 저장
            UserEntity registeredUser=userService.create(user);
            UserDTO responseUserDTO=UserDTO.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }catch(Exception e){
            ResponseDTO responseDTO=ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
        UserEntity user=userService.getByCredentials(
                userDTO.getUsername(),
                userDTO.getPassword());

                if(user!=null){
                    final UserDTO responseUserDTO=UserDTO.builder()
                            .username(user.getUsername())
                            .id(user.getId())
                            .build();
                    return ResponseEntity.ok().body(responseUserDTO);
                }else{
                    ResponseDTO responseDTO=ResponseDTO.builder()
                            .error("login failed.")
                            .build();
                    return ResponseEntity
                            .badRequest()
                            .body(responseDTO);
                }
    }
}
