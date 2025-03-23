package com.highload.architect.soc.network.api;

import com.highload.architect.soc.network.model.LoginPost200Response;
import com.highload.architect.soc.network.model.LoginPostRequest;
import com.highload.architect.soc.network.service.AccountInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.util.UUID;

@RestController
public class LoginApiImpl implements LoginApi {
    private final AccountInfoService accountInfoService;

    public LoginApiImpl(AccountInfoService accountInfoService) {
        this.accountInfoService = accountInfoService;
    }

    @Override
    public ResponseEntity<LoginPost200Response> loginPost(LoginPostRequest loginPostRequest) {
        boolean exist = accountInfoService.existByUserIdAndPassword(loginPostRequest.getId(), loginPostRequest.getPassword());

        if (!exist) {
            throw new RuntimeException(new AccountNotFoundException());
        }

        LoginPost200Response response = new LoginPost200Response();
        response.setToken(UUID.randomUUID().toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
