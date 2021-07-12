package com.sparta.todayrecipe.controller;


import com.sparta.todayrecipe.dto.SignupRequestDto;

import com.sparta.todayrecipe.dto.UserAuthResponse;
import com.sparta.todayrecipe.security.UserDetailsImpl;
import com.sparta.todayrecipe.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성합니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
@Api(value = "UserController V1")
public class UserController {

    private final UserService userService;

    //현재 로그인 된 사용자의 정보와, 현재 로그인 되어있는지 여부
    @ApiOperation("현재 로그인 된 사용자의 정보와, 현재 로그인 되어있는지 여부")
    @GetMapping("/api/user")
    public UserAuthResponse showUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return (userDetails == null) ? UserAuthResponse.empty() :
                UserAuthResponse.builder()
                .username(userDetails.getUsername())
                .build();
    }


    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public Map registerUser(@Valid @RequestBody SignupRequestDto signupRequestDto, Errors errors) {
        // 반환타입 : Map(=return user). user는 Map<String, String> user를 의미함.
        Map<String, String> user = new HashMap<>(); //HashMap? Map인터페이스의 한종류로써 Key와 Value 값으로 데이터를 저장하는 형태를 가짐.
        if (errors.hasErrors()) {

            // 유효성 통과 못한 필드와 메시지를 핸들링/// 갓 선용 !!!!!!!!!
            Map<String, String> validatorResult = userService.validateHandling(errors);
            // validateHandling을 돌고 나온 결과 (ex : {valid_username : "유저명은 필수 입력 값입니다"}, {valid_email : "이메일 형식에 맞지 않습니다.")
            for (String key : validatorResult.keySet()) {
                // 여러개 나온 validateHandling의 결과를 keySet에 한번에 담고, 이를 key라는 이름으로 하나씩 반복하여 꺼냄.
                user.put(key, validatorResult.get(key));
            }
            return user; // 어쨋든 - 유효성검사를 돌고 나온 결과값을 딕셔너리 형태(Map)로 return해준다.

        }
        // 유효성검사 결과 통과 시, "error" : null 반환하고 db에 저장.
        // 왜 null인가? registerUser에서 아이디 중복확인, 비밀번호 입력 일치여부 등을 판단함. 오류가 없음을 null로 나타내기 때문임.
        user.put("error",userService.registerUser(signupRequestDto));

        return user;
    }

    // 카카오톡
    @GetMapping("/user/kakao/callback")
    public void kakaoLogin(String code) {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        userService.kakaoLogin(code);

    }

}
