package com.newneek.user.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newneek.user.UserService;
import com.newneek.user.DTO.UserDTO;
import com.newneek.user.DTO.ResponseDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    // MemberService를 의존성 주입 받음
    @Autowired
    private final UserService userService;

    // 생성자 주입 방법도 사용할 수 있음
    // public AuthController(MemberService memberService) {
    //     this.memberService = memberService;
    // }

    @PostMapping("/Signupform")
    public ResponseDTO<?> signup(@RequestBody UserDTO requestBody) {
        // memberService 인스턴스를 통해 메서드를 호출
        ResponseDTO<?> result = userService.signup(requestBody);  // signup -> join (적절한 메서드명 사용
        System.out.println(requestBody);
        System.out.println(result);
        return result;
    }
}
