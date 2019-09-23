package local.project.Inzynierka.servicelayer.validation;


import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordCreatorService {

    private final PasswordValidator passwordValidator;

    private final PasswordEncoder passwordEncoder;

    public PasswordCreatorService(PasswordValidator passwordValidator, PasswordEncoder passwordEncoder) {
        this.passwordValidator = passwordValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isPasswordValid(String password) {

        return passwordValidator.validate(new PasswordData(password)).isValid();
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean comparePasswordHashes(String password, String oldPasswordHash) {
        return passwordEncoder.matches(password, oldPasswordHash);
    }
}
