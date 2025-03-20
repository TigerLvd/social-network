package com.highload.architect.soc.network.api;

import com.highload.architect.soc.network.model.LoginPost200Response;
import com.highload.architect.soc.network.model.LoginPostRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginApiImpl implements LoginApi {

    @Override
    public ResponseEntity<LoginPost200Response> loginPost(LoginPostRequest loginPostRequest) {
        LoginPost200Response response = new LoginPost200Response();
        response.setToken("asd");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
