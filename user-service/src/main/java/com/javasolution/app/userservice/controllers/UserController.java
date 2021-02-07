package com.javasolution.app.userservice.controllers;

import com.javasolution.app.userservice.services.ConfirmationTokenService;
import com.javasolution.app.userservice.services.MapValidationErrorService;
import com.javasolution.app.userservice.services.UserService;
import com.javasolution.app.userservice.entities.ConfirmationToken;
import com.javasolution.app.userservice.entities.User;
import com.javasolution.app.userservice.requests.LoginRequest;
import com.javasolution.app.userservice.requests.UpdateUserRequest;
import com.javasolution.app.userservice.responses.DeleteAccountResponse;
import com.javasolution.app.userservice.responses.LoginResponse;
import com.javasolution.app.userservice.security.JwtUtil;
import com.javasolution.app.userservice.validators.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserValidator userValidator;
    private final MapValidationErrorService mapValidationErrorService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PutMapping("/users/me")
    public ResponseEntity<?> updateMe(@Valid @RequestBody final UpdateUserRequest updateUserRequest,
                                      final BindingResult result,
                                      final Principal principal) {

        final ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;

        final User user = userService.updateMe(updateUserRequest, principal);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable final String userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(new DeleteAccountResponse("Account with ID: '" + userId + "' deleted successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<?> deleteAccount(final Principal principal) {

        userService.deleteAccount(principal);

        return new ResponseEntity<>(new DeleteAccountResponse("Account deleted successfully"), HttpStatus.OK);
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> getMe(final Principal principal) {

        final User user = userService.getMe(principal);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody final LoginRequest loginRequest, final BindingResult result) throws Exception {

        final ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);

        if (errorMap != null) return errorMap;

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @GetMapping("/users/sign-up/confirm")
    ResponseEntity<?> confirmMail(@RequestParam("token") final String token) {

        final Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);
        optionalConfirmationToken.ifPresent(userService::confirmUser);
        return new ResponseEntity<>("verified email", HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@Valid @RequestBody final User user){
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/users/customers")
    public ResponseEntity<?> getAllCustomers(){
        return new ResponseEntity<>(userService.getAllCustomers(), HttpStatus.OK);
    }

    @PostMapping("/users/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody final User user, final BindingResult result) {

        userValidator.validate(user, result);
        final ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);

        if (errorMap != null) return errorMap;

        final User newUser = userService.signUpUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        final Iterable<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable final long userId) {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @GetMapping("/users/details")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email){
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/scheduling/{userId}")
    User getUserScheduling(@PathVariable final long userId) {
        return userService.getUser(userId);
    }
}
