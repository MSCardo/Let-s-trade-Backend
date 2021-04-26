package com.greenfoxacademy.tradebackend.service;



import com.greenfoxacademy.tradebackend.exception.exception.NoSuchUserException;
import com.greenfoxacademy.tradebackend.model.user.User;
import com.greenfoxacademy.tradebackend.repository.UserRepository;
import com.greenfoxacademy.tradebackend.security.Jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {


  private final UserRepository userRepository;
  private final JwtUtil jwtTokenUtil;


  @Override
  public UserDetails loadUserByUsername(String name)
      throws UsernameNotFoundException {
    return userRepository.findUserByUsername(name)
        .orElseThrow(() ->
            new UsernameNotFoundException(
                String.format("User not found")));
  }


  public int enableAppUser(String email) {
    return userRepository.enableAppUser(email);
  }

  public User getUserByToken(String token) throws NoSuchUserException {
    String username = jwtTokenUtil.extractUsername(token);
    return userRepository.findUserByUsername(username).orElseThrow(NoSuchUserException::new);
  }


  public void checkIfNullOrEmptyField(String inputField, String fieldName, List<String> missingParameterList) {
    if (inputField == null || inputField.equals("")) {
      missingParameterList.add(fieldName);
    }
  }

}
