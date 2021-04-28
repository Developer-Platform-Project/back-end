//package com.project.devidea.modules.account.service;
//
//import com.project.devidea.modules.account.Account;
//import com.project.devidea.modules.account.Interest;
//import com.project.devidea.modules.account.MainActivityZone;
//import com.project.devidea.modules.environment.EnvironmentRepository;
//import com.project.devidea.infra.config.security.LoginUser;
//import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
//import com.project.devidea.infra.error.exception.ErrorCode;
//import com.project.devidea.modules.account.dto.*;
//import com.project.devidea.modules.account.event.SendEmailToken;
//import com.project.devidea.modules.account.exception.AccountException;
//import com.project.devidea.modules.account.repository.AccountRepository;
//import com.project.devidea.modules.account.repository.InterestRepository;
//import com.project.devidea.modules.account.repository.MainActivityZoneRepository;
//import com.project.devidea.modules.tagzone.tag.Tag;
//import com.project.devidea.modules.tagzone.tag.TagRepository;
//import com.project.devidea.modules.tagzone.zone.Zone;
//import com.project.devidea.modules.tagzone.zone.ZoneRepository;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.security.NoSuchAlgorithmException;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class AccountServiceImpl implements AccountService {
//
//    // TODO : 구현체가 너무 많은 것을 의존하고 있다. 적절히 분리하기
//    private final BCryptPasswordEncoder passwordEncoder;
//    private final AccountRepository accountRepository;
//    private final AuthenticationManager authenticationManager;
//    private final JwtTokenUtil jwtTokenUtil;
//    private final ModelMapper modelMapper;
//    private final ZoneRepository zoneRepository;
//    private final TagRepository tagRepository;
//    private final InterestRepository interestRepository;
//    private final MainActivityZoneRepository mainActivityZoneRepository;
//    private final ApplicationEventPublisher publisher;
//    private final EnvironmentRepository environmentRepository;
//    private final String OAUTH_PASSWORD = "dev_idea_oauth_password";
//
//    // TODO : CommonSignUpServiceImpl
//    @Override
//    public SignUp.Response signUp(SignUp.CommonRequest signUpRequestDto) {
//        Account savedAccount = saveAccount(signUpRequestDto);
//        publisher.publishEvent(SendEmailToken.builder()
//                .token(savedAccount.getEmailCheckToken())
//                .to(savedAccount.getEmail()).build());
//        return modelMapper.map(savedAccount, SignUp.Response.class);
//    }
//
//    // TODO : LoginServiceImpl 공통로직
//    private Map<String, String> createToken(String email, JwtTokenUtil tokenUtil){
//        String jwtToken = tokenUtil.generateToken(email);
//        return tokenUtil.createTokenMap(jwtToken);
//    }
//
//    // TODO : OAuthSignUpServiceImpl
//    @Override
//    public SignUp.Response signUpOAuth(SignUp.OAuthRequest request) {
//
//        Account savedAccount = saveOAuthAccount(request);
//        savedAccount.generateEmailToken();
//
//        return SignUp.Response.builder().provider(savedAccount.getProvider())
//                .id(savedAccount.getId().toString()).name(savedAccount.getName())
//                .emailCheckToken(savedAccount.getEmailCheckToken()).build();
//    }
//
//    // TODO : CommonSignUpServiceImpl
//    public Account saveAccount(SignUp.CommonRequest request) {
//        Account account = Account.createAccount(request, passwordEncoder);
//        account.generateEmailToken();
//        return accountRepository.save(account);
//    }
//
//    // TODO : OAuthSignUpServiceImpl
//    @Override
//    public Account saveOAuthAccount(SignUp.OAuthRequest request) {
//        return accountRepository.save(Account.createOAuthAccount(request, passwordEncoder, OAUTH_PASSWORD));
//    }
//
//    // TODO : LoginServiceImpl 공통로직
//    @Override
//    public Map<String, String> login(Login.Common login) throws Exception {
//        authenticate(login.getEmail(), login.getPassword());
//        return createLoginResponse(createToken(login.getEmail(), jwtTokenUtil));
//    }
//
//    // TODO : OAuthLoginServiceImpl
//    @Override
//    public Map<String, String> loginOAuth(Login.OAuth login) throws Exception {
//        Login.Common request = Login.Common.builder()
//                .email(login.getEmail())
//                .password(OAUTH_PASSWORD).build();
//        login(request);
//        return createLoginResponse(createToken(request.getEmail(), jwtTokenUtil));
//    }
//
//    // TODO : LoginServiceImpl 공통로직
//    private Map<String, String> createLoginResponse(Map<String, String> response) {
//        String email = jwtTokenUtil.getUsernameFromToken(response.get("token").substring(7));
//        Account account = accountRepository.findByEmail(email).get();
//        response.put("name", account.getName());
//        response.put("nickname", account.getNickname());
//        if (!account.isSaveDetail()) {
//            response.put("savedDetail", "false");
//            response.put("emailCheckToken", account.getEmailCheckToken());
//        } else {
//            response.put("savedDetail", "true");
//        }
//        return response;
//    }
//
//    // TODO : LoginServiceImpl 공통로직
//    private void authenticate(String email, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
//        } catch (DisabledException e) {
//            throw new DisabledException("이미 탈퇴한 회원입니다.", e);
//        } catch (BadCredentialsException e) {
//            throw new AccountException(ErrorCode.OK, "가입되지 않거나, 메일과 비밀번호가 맞지 않습니다.",
//                    Login.Response.builder().savedDetail(false).emailCheckToken(null).build());
//        }
//    }
//
//    // TODO : SignUpDetailServiceImpl
//    @Override
//    public void saveSignUpDetail(SignUp.DetailRequest req) {
////        token이 없을 경우 에러 발생시키기
//        Account account = accountRepository.findByTokenWithMainActivityZoneAndInterests(req.getToken());
//
////        활동지역(mainActivityZones)
//        Map<String, List<String>> cityProvince = req.splitCitiesAndProvinces();
//        List<Zone> zones = zoneRepository
//                .findByCityInAndProvinceIn(cityProvince.get("city"), cityProvince.get("province"));
//        Set<MainActivityZone> mainActivityZones = getMainActivityZones(account, zones);
//
////        관심기술(Interest)
//        List<Tag> tags = tagRepository.findByFirstNameIn(req.getInterests());
//        Set<Interest> interests = getInterests(account, tags);
//
////        연관관계 설정하기
//        account.saveSignUpDetail(req, mainActivityZones, interests);
//
////        매핑 테이블 데이터들 save
//        mainActivityZoneRepository.saveAll(mainActivityZones);
//        interestRepository.saveAll(interests);
//    }
//
//    // TODO : CommonSignUpServiceImpl Common만!
//    @Override
//    public String authenticateEmailToken(String email, String token) {
//        Account account = accountRepository.findByEmail(email).orElseThrow(
//                () -> new AccountException("회원을 찾을 수 없습니다.", ErrorCode.ACCOUNT_ERROR));
//        account.validateToken(token);
//        return getFrontURL();
//    }
//
//    // TODO : CommonSignUpServiceImpl
//    public String getFrontURL() {
//        return environmentRepository.findByDescription("FRONT").getUrl();
//    }
//
////  TODO 여기서 부터는 Update 영역=======================================================================================
//    @Override
//    @Transactional(readOnly = true)
//    public Update.ProfileResponse getProfile(LoginUser loginUser) {
//        return modelMapper.map(loginUser.getAccount(), Update.ProfileResponse.class);
//    }
//
//    @Override
//    public void updateProfile(LoginUser loginUser, Update.ProfileRequest request) {
//        Account account = accountRepository.findByEmail(loginUser.getUsername()).orElseThrow();
//        account.updateProfile(request);
//    }
//
//    @Override
//    public void updatePassword(LoginUser loginUser,
//                               Update.PasswordRequest request) {
//        Account account = accountRepository.findByEmail(loginUser.getUsername()).orElseThrow();
//        account.updatePassword("{bcrypt}" + passwordEncoder.encode(request.getPassword()));
//    }
//
//    @Override
//    public Update.Interest getAccountInterests(LoginUser loginUser) {
//        Set<Interest> interests =
//                accountRepository.findByEmailWithInterests(loginUser.getUsername()).getInterests();
//        return Update.Interest.builder().interests(
//                interests.stream()
//                        .map(interest -> interest.getTag().getFirstName())
//                        .collect(Collectors.toList())).build();
//    }
//
//    @Override
//    public void updateAccountInterests(LoginUser loginUser, Update.Interest request) {
//
//        Account account = accountRepository.findByEmailWithInterests(loginUser.getUsername());
//        List<Tag> tags = tagRepository.findByFirstNameIn(request.getInterests());
//        Set<Interest> interests = getInterests(account, tags);
//
////        기존에 있던 정보들 삭제
//        interestRepository.deleteByAccount(account);
//
////        연관관계 메서드
//        account.updateInterests(interests);
//        interestRepository.saveAll(interests);
//    }
//
//    @Override
//    public Update.MainActivityZone getAccountMainActivityZones(LoginUser loginUser) {
//        Set<MainActivityZone> mainActivityZones =
//                accountRepository.findByEmailWithMainActivityZones(loginUser.getUsername()).getMainActivityZones();
//        return Update.MainActivityZone.builder()
//                .mainActivityZones(
//                        mainActivityZones.stream().map(zone -> zone.getZone().toString()).collect(Collectors.toList()))
//                .build();
//    }
//
//    @Override
//    public void updateAccountMainActivityZones(LoginUser loginUser,
//                                               Update.MainActivityZone request) {
//        Account account = accountRepository.findByEmailWithMainActivityZones(loginUser.getUsername());
//        Map<String, List<String>> map = request.splitCityAndProvince();
//        List<Zone> zones
//                = zoneRepository.findByCityInAndProvinceIn(map.get("city"), map.get("province"));
//        Set<MainActivityZone> mainActivityZones = getMainActivityZones(account, zones);
//
////        기존에 있던 정보들 삭제
//        mainActivityZoneRepository.deleteByAccount(account);
//
////        연관관계 메서드
//        account.updateMainActivityZones(mainActivityZones);
//        mainActivityZoneRepository.saveAll(mainActivityZones);
//    }
//
//    @Override
//    public Map<String, String> getAccountNickname(LoginUser loginUser) {
//        Map<String, String> map = new HashMap<>();
//        map.put("nickname", loginUser.getAccount().getNickname());
//        return map;
//    }
//
//    @Override
//    public void updateAccountNickname(LoginUser loginUser, Update.NicknameRequest request) {
//        Account account = accountRepository.findByEmail(loginUser.getUsername()).orElseThrow();
//        account.changeNickname(request.getNickname());
//    }
//
//    @Override
//    public Update.Notification getAccountNotification(LoginUser loginUser) {
//        Account account = loginUser.getAccount();
//        return modelMapper.map(account, Update.Notification.class);
//    }
//
//    @Override
//    public void updateAccountNotification(LoginUser loginUser, Update.Notification request) {
//        Account account = accountRepository.findByEmail(loginUser.getUsername()).orElseThrow();
//        account.updateNotifications(request);
//    }
//
//    @Override
//    public void quit(LoginUser loginUser) {
//        Account account = accountRepository.findByEmail(loginUser.getUsername()).orElseThrow();
//        account.changeToQuit();
//    }
//}
