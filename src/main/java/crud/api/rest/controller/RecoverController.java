package crud.api.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import crud.api.rest.ErrorObject;
import crud.api.rest.model.User;
import crud.api.rest.repository.UserRepository;


public class RecoverController {
    
    @Autowired
    UserRepository userRepository;

    @PutMapping(value = "/forgotpassword", produces = "application/json")
	public ResponseEntity<ErrorObject> RecoverPassword(@RequestBody User user) { 
        ErrorObject errorResponse = new ErrorObject();

        if(user != null){
            //enviar email
            /* */

            errorResponse.setHttpStatus(HttpStatus.OK);
            errorResponse.setMessage("Email de confirmação não enviado!");
        }else{
            errorResponse.setHttpStatus(HttpStatus.NOT_FOUND);
            errorResponse.setMessage("Usuário não encontrado!");
        }

		return new ResponseEntity<ErrorObject>(errorResponse, errorResponse.getHttpStatus());
	}
}
