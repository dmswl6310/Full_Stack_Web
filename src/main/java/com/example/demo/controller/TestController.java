//package com.example.demo.controller;
//
//import com.example.demo.dto.ResponseDTO;
//import com.example.demo.dto.TestRequestBodyDTO;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("test")
//public class TestController {
//    @GetMapping
//    public String testController(){
//        return "Hello World!";
//    }
//
//    @GetMapping("/testGetMapping")
//    public String testControllerWithPath(){
//        return "Hello world! testGetMapping";
//    }
//
//    @GetMapping("/{id}")
//    public String testControllerwithPathVariables(@PathVariable(required=false)int id){
//        return "Hello world! ID"+id;
//    }
//
//    @GetMapping("/testRequestParam")
//    public String testControllerRequestParam(@RequestParam(required=false) int id) {
//        return "Hello world! ID"+id;
//    }
//
//    @GetMapping("/testRequestBody")
//    public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO){
//        return "Hello World ID" + testRequestBodyDTO.getId()+" Message : "+testRequestBodyDTO.getMessage();
//    }
//
//    @GetMapping("/testResponseBody")
//    public ResponseDTO<String> testControllerResponseBody(){
//        List<String> list=new ArrayList<>();
//        list.add("Hello World! I'm ResponseDTO");
//        ResponseDTO<String> response=ResponseDTO.<String>builder().data(list).build();
//        return response;
//    }
//
//    @GetMapping("testResponseEntity")
//    public ResponseEntity<?> testControllerResponseEntity(){
//        List<String> list=new ArrayList<>();
//        list.add("Hello world! I'm ResponseEntity. And you got 400!");
//        ResponseDTO<String> response=ResponseDTO.<String>builder().data(list).build();
//        //http statud를 400으로 설정
//        return ResponseEntity.badRequest().body(response);
//    }
//}
