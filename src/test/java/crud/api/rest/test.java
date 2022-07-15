package crud.api.rest;

import crud.api.rest.model.User;
import crud.api.rest.repository.UserRepository;

public class test {
    
    //@PutMapping(value = "/", produces = "application/json")
    public ResponseEntity<StatusObject> update(@RequestBody User user){
		User currentUser = userRepository.findById(user.getId()).orElse(null);
        
        if(user != null){
			if(currentUser != null){
				user = validUserFields(user);
                
                if(user != null){
					mail.sendAuthCodeEmail(user, "Foi solicitado a alteração dos dados da sua conta.");
                    
                    if(/* Se email for confirmado */){
						userRepository.save(user); 
						statusResponse.setHttpStatus(HttpStatus.OK);
						statusResponse.setMessage("Usuário atualizado com sucesso!");
                    }
                }
            }else{
				statusResponse.setHttpStatus(HttpStatus.NOT_FOUND);
				statusResponse.setMessage("Usuário não encontrado!");
            }
        }else{
			statusResponse.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
			statusResponse.setMessage("Usuário inválido!");
        }

		return new ResponseEntity<StatusObject>(statusResponse, HttpStatus.OK);
    }

	////
	
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<StatusObject> Delete(@PathVariable("id") long id){
		User user = userRepository.findById(id).orElse(null);

		statusResponse.setHttpStatus(HttpStatus.NOT_FOUND);
		statusResponse.setMessage("Usuário não encontrado!");
		
		if(user != null){
			mail.sendAuthCodeEmail(user, "Foi solicitado a exclusão da sua conta.");

			if(/* Se email for confirmado */){
				userRepository.deleteById(id);
				statusResponse.setHttpStatus(HttpStatus.OK);
				statusResponse.setMessage("Usuário deletado com sucesso!");
			}
		}
		
		return new ResponseEntity<StatusObject>(statusResponse, HttpStatus.OK);
	}
    
}
