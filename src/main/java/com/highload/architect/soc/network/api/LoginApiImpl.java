package com.highload.architect.soc.network.api;

import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.LoginPost200Response;
import com.highload.architect.soc.network.model.LoginPostRequest;
import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.service.AccountInfoService;
import com.highload.architect.soc.network.service.SimpleTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.util.UUID;

@RestController
public class LoginApiImpl implements LoginApi {
    private final AccountInfoService accountInfoService;
    private final PasswordEncoder encoder;
    private final SimpleTokenService simpleTokenService;

    public LoginApiImpl(AccountInfoService accountInfoService, SimpleTokenService simpleTokenService) {
        this.accountInfoService = accountInfoService;
        this.simpleTokenService = simpleTokenService;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public ResponseEntity<LoginPost200Response> loginPost(LoginPostRequest loginPostRequest) {
        UUID userInfoId = UUID.fromString(loginPostRequest.getId());
        AccountInfo accountInfo = accountInfoService.getById(userInfoId);
        boolean exist = encoder.matches(loginPostRequest.getPassword(), accountInfo.getPassword());

        if (!exist) {
            throw new RuntimeException(new AccountNotFoundException());
        }
        SimpleToken simpleToken = simpleTokenService.createSimpleToken(userInfoId);

        LoginPost200Response response = new LoginPost200Response();
        response.setToken(simpleToken.getId().toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
