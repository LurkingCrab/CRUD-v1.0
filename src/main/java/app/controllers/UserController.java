package app.controllers;

import app.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.NoResultException;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserDetailsService service;

    @Autowired
    public UserController(UserDetailsService service) {
        this.service = service;
    }

    @GetMapping()
    public String getUserPage(@AuthenticationPrincipal User user, Model model) {
        try {
            Optional<UserDetails> userOptional =
                    Optional.ofNullable(service.loadUserByUsername(user.getLogin()));
            userOptional.ifPresent(userDetails -> model.addAttribute("user", userDetails));
        } catch (NoResultException ignore) {
            return "errors/not_found";
        }
        return "user/user";
    }
}