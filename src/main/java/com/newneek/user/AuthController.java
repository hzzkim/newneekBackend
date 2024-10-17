package com.newneek.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO requestBody) {
        ResponseEntity<?> result = userService.login(requestBody);
        return result;

    }


}
