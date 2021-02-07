package com.javasolution.app.userservice;

import com.javasolution.app.userservice.entities.User;
import com.javasolution.app.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.javasolution.app.userservice.entities.UserRole.*;
import static com.javasolution.app.userservice.security.SecurityConstants.ADMIN_EMAIL;

@Component
public class Init {

    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Init(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init(){
        addAdmin();
        addResource();
        addCustomer();
        addStaff();
    }

    private void addAdmin(){
        User admin = new User();
        admin.setEmail(ADMIN_EMAIL);
        admin.setName("Admin");
        admin.setSurname("Admin");
        admin.setPassword(bCryptPasswordEncoder.encode("Admin"));
        admin.setUserRole(ROLE_ADMIN);
        admin.setEnabled(true);
        admin.setSsn("123456789");

        if(!userRepository.findByEmail(ADMIN_EMAIL).isPresent())
            userRepository.save(admin);
    }

    private void addResource(){
        User resource = new User();
        resource.setEmail("dawulf97@gmail.com");
        resource.setName("Resource");
        resource.setSurname("Resource");
        resource.setPassword(bCryptPasswordEncoder.encode("Resource"));
        resource.setUserRole(ROLE_RESOURCE);
        resource.setEnabled(true);
        resource.setSsn("987654321");

        if(!userRepository.findByEmail("dawulf97@gmail.com").isPresent())
            userRepository.save(resource);
    }

    private void addCustomer(){
        User customer = new User();
        customer.setEmail("dawid_19_97@interia.pl");
        customer.setName("Customer");
        customer.setSurname("Customer");
        customer.setPassword(bCryptPasswordEncoder.encode("Customer"));
        customer.setUserRole(ROLE_CUSTOMER);
        customer.setEnabled(true);
        customer.setSsn("123321456");

        if(!userRepository.findByEmail("dawid_19_97@interia.pl").isPresent())
            userRepository.save(customer);
    }

    private void addStaff(){
        User staff = new User();
        staff.setEmail("bestmentoringforstudents@gmail.com");
        staff.setName("Staff");
        staff.setSurname("Staff");
        staff.setPassword(bCryptPasswordEncoder.encode("Staff"));
        staff.setUserRole(ROLE_STAFF);
        staff.setEnabled(true);
        staff.setSsn("987789654");

        if(!userRepository.findByEmail("bestmentoringforstudents@gmail.com").isPresent())
            userRepository.save(staff);
    }
}
