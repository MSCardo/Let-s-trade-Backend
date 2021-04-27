//package com.greenfoxacademy.tradebackend.services;
//
//import com.greenfoxacademy.tradebackend.exception.exception.UserException;
//import com.greenfoxacademy.tradebackend.model.register.RegisterResponseDTO;
//import com.greenfoxacademy.tradebackend.model.register.RegistrationRequestDTO;
//import com.greenfoxacademy.tradebackend.model.user.User;
//import com.greenfoxacademy.tradebackend.model.user.UserRole;
//import com.greenfoxacademy.tradebackend.repository.UserRepository;
//import com.greenfoxacademy.tradebackend.security.Jwt.JwtUtil;
//import com.greenfoxacademy.tradebackend.security.Jwt.MyUserDetailsService;
//import com.greenfoxacademy.tradebackend.security.confirmationToken.ConfirmationTokenService;
//import com.greenfoxacademy.tradebackend.service.RegistrationService;
//import com.greenfoxacademy.tradebackend.service.SignUpService;
//import com.greenfoxacademy.tradebackend.service.UserService;
//import com.greenfoxacademy.tradebackend.service.email.EmailSender;
//import com.greenfoxacademy.tradebackend.service.email.EmailService;
//import com.greenfoxacademy.tradebackend.service.email.EmailValidator;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.springframework.security.authentication.AuthenticationManager;
//
//import javax.jws.soap.SOAPBinding;
//import java.util.Arrays;
//
//import static org.hamcrest.Matchers.any;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.when;
//
//public class RegistrationServiceTest {
//
//  private RegistrationService registrationService;
//  private UserService userService;
//  private SignUpService signUpService;
//  private EmailValidator emailValidator;
//  private ConfirmationTokenService confirmationTokenService;
//  private EmailSender emailSender;
//  private EmailService emailService;
//
//  @Before
//  public void setUp() {
//    userService = Mockito.mock(UserService.class);
//    signUpService = Mockito.mock(SignUpService.class);
//    emailValidator = Mockito.mock(EmailValidator.class);
//    confirmationTokenService = Mockito.mock(ConfirmationTokenService.class);
//    emailSender = Mockito.mock(EmailSender.class);
//    emailService = Mockito.mock(EmailService.class);
//    registrationService =
//        new RegistrationService(userService, signUpService,
//            emailValidator,
//            confirmationTokenService, emailSender, emailService);
//  }
//
//  @Test
//  public void register_HappyCase()
//      throws UserException {
//    String username = "DummyUser";
//    String email = "test@email.com";
//    String password = "password";
//    User mockUser = new User(username, email, password, UserRole.USER);
//    mockUser.setId(1L);
//    RegistrationRequestDTO registrationData = new RegistrationRequestDTO(username, email, password);
//
//    when(mockUserRepository.findAllUsername()).thenReturn(Arrays.asList("TestUser", "TestUser1"));
//    when(mockUserRepository.save(any(User.class))).thenReturn(mockUser);
//
//    RegisterResponseDTO mockResponse = new RegisterResponseDTO(username, email, password );
//
//    RegisterResponseDTO response = registrationService.register(registrationData);
//    assertEquals(mockResponse, response);
//  }
//
//  @Test(expected = ReservedUsernameException.class)
//  public void createUser_DupicateUsername_ReservedUsernameExceptionExpected()
//      throws UserException, ReservedKingdomnameException, NoSuchKingdomException, NotYourBuildingException {
//
//    String username = "TestUser";
//    String password = "password";
//    String kingdomname = "DummyKingdom";
//    RegisterRequestDTO registrationData = new RegisterRequestDTO(username, password, kingdomname);
//
//    when(mockUserRepository.findAllUsername()).thenReturn(Arrays.asList("TestUser", "TestUser1"));
//
//    userService.createUser(registrationData);
//  }
//
//  @Test(expected = ReservedKingdomnameException.class)
//  public void createUser_DupicateKingdomname_ReservedKingdomnameExceptionExpected()
//      throws UserException, ReservedKingdomnameException, NoSuchKingdomException, NotYourBuildingException {
//    String username = "DummyUser";
//    String password = "password";
//    String kingdomname = "TestKingdom";
//    RegisterRequestDTO registrationData = new RegisterRequestDTO(username, password, kingdomname);
//
//    when(mockKingdomService.isKingdomnameOccupied(kingdomname)).thenReturn(true);
//    when(mockUserRepository.findAllUsername()).thenReturn(Arrays.asList("TestUser", "TestUser1"));
//
//    userService.createUser(registrationData);
//  }
//
//  @Test(expected = MissingParameterException.class)
//  public void createUser_MissingParameterExceptionExpected()
//      throws UserException, ReservedKingdomnameException, NoSuchKingdomException, NotYourBuildingException {
//    String username = "DummyUser";
//    RegisterRequestDTO registrationData = new RegisterRequestDTO(username);
//
//    when(mockUserRepository.findAllUsername()).thenReturn(Arrays.asList("TestUser", "TestUser1"));
//    when(mockKingdomService.getAllKingdomname()).thenReturn(Arrays.asList("TestKingdom", "TestKingdom1"));
//
//    userService.createUser(registrationData);
//  }
//
//  @Test(expected = MissingParameterException.class)
//  public void createUser_EmptyStringAsParameter_MissingParameterExceptionExpected()
//      throws UserException, ReservedKingdomnameException, NoSuchKingdomException, NotYourBuildingException {
//    String username = "DummyUser";
//    RegisterRequestDTO registrationData = new RegisterRequestDTO(username);
//
//    when(mockUserRepository.findAllUsername()).thenReturn(Arrays.asList("TestUser", "TestUser1"));
//    when(mockKingdomService.getAllKingdomname()).thenReturn(Arrays.asList("TestKingdom", "TestKingdom1"));
//
//    userService.createUser(registrationData);
//  }
//
//  @Test(expected = MissingParameterException.class)
//  public void createUser_whenNullRegisterData_MissingParameterExceptionExpected()
//      throws UserException, ReservedKingdomnameException, NoSuchKingdomException, NotYourBuildingException {
//    RegisterRequestDTO registrationData = null;
//
//    userService.createUser(registrationData);
//  }
//}
