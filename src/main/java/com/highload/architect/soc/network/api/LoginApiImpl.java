package com.highload.architect.soc.network.api;

import com.highload.architect.soc.network.exception.InvalidCredentialsException;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.LoginPost200Response;
import com.highload.architect.soc.network.model.LoginPostRequest;
import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.service.AccountInfoService;
import com.highload.architect.soc.network.service.SimpleTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class LoginApiImpl implements LoginApi {
    private static final Logger log = LoggerFactory.getLogger(LoginApiImpl.class);
    
    private final AccountInfoService accountInfoService;
    private final PasswordEncoder encoder;
    private final SimpleTokenService simpleTokenService;

    public LoginApiImpl(AccountInfoService accountInfoService, 
                       SimpleTokenService simpleTokenService,
                       PasswordEncoder encoder) {
        this.accountInfoService = accountInfoService;
        this.simpleTokenService = simpleTokenService;
        this.encoder = encoder;
    }

    @Override
    public ResponseEntity<LoginPost200Response> loginPost(LoginPostRequest loginPostRequest) {
        log.info("Login attempt for user ID: {}", loginPostRequest.getId());
        
        try {
            UUID userInfoId = UUID.fromString(loginPostRequest.getId());
            AccountInfo accountInfo = accountInfoService.getById(userInfoId);
            
            boolean passwordMatches = encoder.matches(loginPostRequest.getPassword(), accountInfo.getPassword());
            
            if (!passwordMatches) {
                log.warn("Invalid credentials for user ID: {}", userInfoId);
                throw new InvalidCredentialsException("Invalid username or password");
            }
            
            SimpleToken simpleToken = simpleTokenService.createSimpleToken(userInfoId);
            
            LoginPost200Response response = new LoginPost200Response();
            response.setToken(simpleToken.getId().toString());
            
            log.info("Login successful for user ID: {}", userInfoId);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID format: {}", loginPostRequest.getId());
            return ResponseEntity.badRequest().build();
        } catch (InvalidCredentialsException e) {
            log.warn("Invalid credentials: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Error during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
