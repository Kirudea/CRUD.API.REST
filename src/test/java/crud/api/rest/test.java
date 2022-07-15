package crud.api.rest;

import crud.api.rest.model.User;
import crud.api.rest.repository.UserRepository;

public class test {
    
    public void create(User user){
        if(user != null){
            if(user.getId() == 0){
                user = validUserFields(user);
                
                if(user != null){
                    mail.sendAuthCodeEmail(user, "O email "+user.getEmail()+" foi informado para utilização do sistema MySystem.");
                    
                    if(/* Se email for confirmado */){
                       userRepository.save(user); 
                    }
                }
            }else{
                //ID foi informado
            }
        }else {
            //user inválido
        }
    }
    
    public void update(User user){
        User currentUser = userRepository.findById(user.getId()).orElse(null);
        
        if(user != null){
            if(currentUser != null){
                user = validUserFields(user);
                
                if(user != null){
                    mail.sendAuthCodeEmail(user, "Foi solicitado a alteração dos dados da sua conta.");
                    
                    if(/* Se email for confirmado */){
                       userRepository.save(user); 
                    }
                }
            }else{
                //user not found
            }
        }else{
            //user inválido
        }
    }
    
}
