package datn.backend.service.impl;

import datn.backend.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public String generateOtp() {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int n = alphabet.length();

        StringBuilder result = new StringBuilder();
        Random r = new Random();

        for (int i = 0; i < 6; i++) {
            char randomChar = alphabet.charAt(r.nextInt(n));
            result.append(randomChar);
        }
        return result.toString();
    }
}
