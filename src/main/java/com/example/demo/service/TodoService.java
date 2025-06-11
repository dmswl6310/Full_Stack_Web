package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TodoService {
    @Autowired
    private TodoRepository repository;

    public List<TodoEntity> create(final TodoEntity entity){
        validate(entity);

        repository.save(entity);
        log.info("Entity Id : {} is saved.",entity.getId());
        return repository.findByUserId(entity.getUserId());
    }

    private void validate(final TodoEntity entity){
        if(entity==null){
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(entity.getUserId()==null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user");
        }
    }

    public List<TodoEntity> update(final TodoEntity entity){
        validate(entity);

        // 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져옴
        final Optional<TodoEntity> original= repository.findById(entity.getId());

        original.ifPresent(todo->{
            // 반환된 TodoEntity가 존재하면 값을 덮어씌움
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            // 데이터베이스에 새 값 저장
            repository.save(todo);
        });

        return retrieve(entity.getUserId());
    }

    public List<TodoEntity> retrieve(final String userId){
        return repository.findByUserId(userId);
    }

    public String testService(){
        // TodoEntity 생성
        TodoEntity entity=TodoEntity.builder().title("My first todo item").build();
        // TodoEntity 저장
        repository.save(entity);
        // TodoEntity 검색
        TodoEntity savedEntity=repository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }

    public List<TodoEntity> delete(final TodoEntity entity){
        validate(entity);

        try{
            repository.delete(entity);
        }catch(Exception e){
            log.error("error deleting entity ",entity.getId(),e);

            throw new RuntimeException("error deleting entity "+entity.getId());
        }
        return retrieve(entity.getUserId());
    }
}
