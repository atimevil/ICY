package com.sparta.icy.service;

import com.sparta.icy.dto.SignupRequestDto;
import com.sparta.icy.entity.User;
import com.sparta.icy.entity.UserStatus;
import com.sparta.icy.error.AlreadySignedOutUserCannotBeSignoutAgainException;
import com.sparta.icy.error.DuplicateUsernameException;
import com.sparta.icy.error.PasswordDoesNotMatchException;
import com.sparta.icy.error.ResignupWithSignedoutUsernameException;
import com.sparta.icy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
   // private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new DuplicateUsernameException("중복된 사용자가 존재합니다.");
        }

        //탈퇴한 회원인지 확인
        User checkUser = userRepository.findByUsername(username).orElseThrow();
        if(checkUser.getStatus()==UserStatus.SECESSION){
            throw new ResignupWithSignedoutUsernameException("탈퇴한 아이디로 재가입이 불가합니다.");
        }

       //회원 상태 등록
        UserStatus status=UserStatus.IN_ACTION;


        // 사용자 등록
        //String username, String nickname, String password, String email, String intro, UserStatus status
        User user = new User(username, requestDto.getNickname(), password, requestDto.getEmail(), requestDto.getIntro(), status);
        userRepository.save(user);
    }

    public boolean signout(String userDetailsUsername, String password) {
       try{
           User checkUsername = userRepository.findByUsername(userDetailsUsername).orElseThrow();


           //이미 탈퇴한 회원이라서 재탈퇴 못함
           if(checkUsername.getStatus()==UserStatus.SECESSION){
               throw new AlreadySignedOutUserCannotBeSignoutAgainException("이미 탈퇴한 회원은 재탈퇴가 불가능");

           }

           //사용자가 입력한 비밀번호가 현재 로그인된 비밀번호와 맞는지 확인
           if(!checkUsername.getPassword().equals(password)){
               throw new PasswordDoesNotMatchException("기존 비밀번호와 일치하지 않음");

           }

           //탈퇴한 회원으로 전환
           checkUsername.setStatus(UserStatus.SECESSION);
           userRepository.save(checkUsername); // 변경된 상태를 저장
           return true;

       }catch (PasswordDoesNotMatchException | AlreadySignedOutUserCannotBeSignoutAgainException e) {
           // 예외 발생 시 로그를 남기고 false 반환
           log.error(e.getMessage(), e);
           return false;
       }

    }
}