package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("todo")
public class TodoController {
    @Autowired
    private TodoService service;

    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
        try{
            // String temporaryUserId="temporary-user"; // temp user id

            // (1) TodoEntity로 변환
            TodoEntity entity=TodoDTO.toEntity(dto);

            // (2) id를 null로 초기화.(생성 당시에는 id 없어야)
            entity.setId(null);

            // (3) 임시 유저 아이디를 설정. (추후 인증 추가 예정)
            entity.setUserId(userId);

            // (4) 서비스를 이용해 Todo 엔티티 생성
            List<TodoEntity> entities=service.create(entity);

            // (5) 자바 스트림을 이용해 엔티티 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // (6) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response=ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // (7) ResponseDTO를 리턴
            return ResponseEntity.ok().body(response);
        }catch(Exception e) {
            // (8) 에러가 날 경우 dto 대신 error에 메시지 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
            }
        }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,@RequestBody TodoDTO dto){
        //String temporaryUserId="temporary-user";

        TodoEntity entity=TodoDTO.toEntity(dto);
        entity.setUserId(userId);
        List<TodoEntity> entities=service.update(entity);
        List<TodoDTO> dtos=entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> response=ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);

    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){
        // String temporaryUserId="temporary-user";
        List<TodoEntity> entities=service.retrieve(userId);
        List<TodoDTO>dtos=entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> response=ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }
//    @GetMapping("/test")
//    public ResponseEntity<?> testTodo(){
//        String str=service.testService();
//        List<String> list=new ArrayList<>();
//        list.add(str);
//
//        ResponseDTO<String> response=ResponseDTO.<String>builder().data(list).build();
//        return ResponseEntity.ok().body(response);
//    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId,@RequestBody TodoDTO dto){
        try{
            // String temporaryUserId="temporary-user"; // temp user id

            TodoEntity entity=TodoDTO.toEntity(dto);
            entity.setUserId(userId);
            List<TodoEntity> entities=service.delete(entity);
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response=ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        }catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
