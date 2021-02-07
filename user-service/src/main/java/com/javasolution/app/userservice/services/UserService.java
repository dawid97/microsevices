package com.javasolution.app.userservice.services;

import com.javasolution.app.userservice.entities.ConfirmationToken;
import com.javasolution.app.userservice.entities.User;
import com.javasolution.app.userservice.entities.UserRole;
import com.javasolution.app.userservice.exceptions.*;
import com.javasolution.app.userservice.proxies.BookingServiceProxy;
import com.javasolution.app.userservice.proxies.ResourceServiceProxy;
import com.javasolution.app.userservice.repositories.ConfirmationTokenRepository;
import com.javasolution.app.userservice.repositories.UserRepository;
import com.javasolution.app.userservice.requests.UpdateUserRequest;
import com.javasolution.app.userservice.responses.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.javasolution.app.userservice.security.SecurityConstants.ADMIN_EMAIL;

@Service
public class UserService implements UserDetailsService {

    @Autowired //must be autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final JavaMailSender javaMailSender;
    private final BookingServiceProxy bookingServiceProxy;
    private final ResourceServiceProxy resourceServiceProxy;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public UserService(final UserRepository userRepository,
                       final ConfirmationTokenService confirmationTokenService,
                       final JavaMailSender javaMailSender,
                       final BookingServiceProxy bookingServiceProxy,
                       final ResourceServiceProxy resourceServiceProxy,
                       final ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenService = confirmationTokenService;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.bookingServiceProxy = bookingServiceProxy;
        this.resourceServiceProxy = resourceServiceProxy;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    void sendConfirmationMail(final String userMail, final String token) throws MessagingException {

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(userMail);
        mimeMessageHelper.setSubject("Mail Confirmation Link!");

        final String message = "<head>" +
                "<style type=\"text/css\">" +
                ".red { color: #f00; }" +
                "</style>" +
                "</head>" +
                "<h1 class=\"red\">Thank you for registering! \uD83D\uDE00</h1>" +
                "<p>" +
                "Please click on the below link to activate your account." +
                "</p>" +
                "<div>" +
                "http://localhost:8765/api/user-service/users/sign-up/confirm?token=" + token +
                "</div>";

        mimeMessage.setContent(message, "text/html; charset=utf-8");
        javaMailSender.send(mimeMessage);
    }

    public User updateMe(final UpdateUserRequest updateUserRequest, final Principal principal) {

        final User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + principal.getName() + " cannot be found."));

        user.setName(updateUserRequest.getName());
        user.setSurname(updateUserRequest.getSurname());

        return userRepository.save(user);
    }

    protected Optional<User> findUser(final String userId) {

        final long id;

        try {
            id = Long.parseLong(userId);
        } catch (final NumberFormatException ex) {
            throw new InvalidCastException("User id have to be long type");
        }

        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " cannot be found."));
    }

    public void deleteUser(final String userId) {

        //check if user exists
        final User user = findUser(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID: '" + userId + "' not found"));

        //check if user has email token
        ConfirmationToken confirmationToken = confirmationTokenRepository.findConfirmationTokenByUserId(user.getId());
        if(confirmationToken != null) {
            confirmationTokenRepository.deleteById(confirmationToken.getId());
        }

        //check if primary admin
        if (user.getEmail() != null && user.getEmail().equals(ADMIN_EMAIL))
            throw new DeleteAccountException("You can not delete account because this is primary admin");

        //find user bookings
        List<Booking> bookings = bookingServiceProxy.getBookingsByUserId(user.getId());

        //user has no bookings
        if(bookings.isEmpty()){
            userRepository.delete(user);
            return;
        }

        //delete user bookings
        for(final Booking booking : bookings){
            bookingServiceProxy.deleteBookingBySlotId(booking.getSlotId());
            //change slot status
            resourceServiceProxy.updateSlot(false, booking.getSlotId());
        }

        userRepository.delete(user);
    }

    public void deleteAccount(final Principal principal) {

        //check if exists
        final User user = findUser(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with ID: '" + principal.getName() + "' not found"));

        //check if primary admin
        if (user.getEmail().equals(ADMIN_EMAIL))
            throw new DeleteAccountException("You can not delete account because this is primary admin");

        userRepository.delete(user);
    }

    public User getMe(final Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: '" + principal.getName() + "' not found"));
    }

    public User signUpUser(final User user) {

        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setConfirmPassword("");

        final User savedUser;

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UsernameAlreadyExistsException("Email '" + user.getEmail() + "' already exists");
        }

        if(userRepository.findBySsn(user.getSsn()).isPresent()){
            throw new UsernameAlreadyExistsException("Ssn '" + user.getSsn() + "' already exists");
        }

        savedUser = userRepository.save(user);

        final ConfirmationToken confirmationToken = new ConfirmationToken(user);
        final ConfirmationToken savedConfirmationToken = confirmationTokenService.saveConfirmationToken(confirmationToken);

        try {
            sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
        } catch (final MessagingException ex) {
            userRepository.delete(user);
            confirmationTokenService.deleteConfirmationToken(savedConfirmationToken.getId());
            throw new UnableSendEmailException("Something went wrong. Please try again later");
        }

        savedUser.setPassword("");

        return savedUser;
    }

    public void confirmUser(final ConfirmationToken confirmationToken) {

        final User user = confirmationToken.getUser();
        user.setEnabled(true);

        userRepository.save(user);

        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(final long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID: '" + userId + "' was not found"));
    }

    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow( () -> new UserNotFoundException("User with email " + email + " was not found"));
    }

    public User addUser(final User user) {

        //check if ssn exist
        if(userRepository.findBySsn(user.getSsn()).isPresent()){
            throw new UsernameAlreadyExistsException("Ssn '" + user.getSsn() + "' already exists");
        }

        return userRepository.save(user);
    }

    public List<User> getAllCustomers() {
        return userRepository.findAllByUserRole(UserRole.ROLE_CUSTOMER);
    }
}
