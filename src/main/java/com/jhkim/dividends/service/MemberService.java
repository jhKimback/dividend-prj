package com.jhkim.dividends.service;

import com.jhkim.dividends.exception.impl.AlreadyExistsUserException;
import com.jhkim.dividends.exception.impl.NoUserIdException;
import com.jhkim.dividends.exception.impl.WrongPasswordException;
import com.jhkim.dividends.persist.entity.MemberEntity;
import com.jhkim.dividends.persist.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.jhkim.dividends.model.Auth;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find the User ->" + username));
    }

    public MemberEntity register(Auth.SignUp member){
        log.info("Register User -> " + member.getUsername());
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if (exists){
            throw new AlreadyExistsUserException();
        }
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result = this.memberRepository.save(member.toEntity());

        return result;
    }

    public MemberEntity authenticate(Auth.SignIn member){
        var user = this.memberRepository.findByUsername(member.getUsername())
                                        .orElseThrow(() -> new NoUserIdException());
        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())){
            throw new WrongPasswordException();
        }
        return user;
    }
}
