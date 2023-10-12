package com.kauecdev.todolist.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kauecdev.todolist.user.User;
import com.kauecdev.todolist.user.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String servletPath = request.getServletPath();    

        if (servletPath.startsWith("/task")) {
            String authorization = request.getHeader("Authorization");
    
            String authorizationEncoded =  authorization.substring("Basic".length()).trim();
    
            byte[] authorizationDecoded = Base64.getDecoder().decode(authorizationEncoded);
    
            String authorizationValue = new String(authorizationDecoded);
    
            String[] credentials = authorizationValue.split(":");
    
            String username = credentials[0];
            String password = credentials[1];

            Optional<User> user = this.userRepository.findByUsername(username);

            if (!user.isPresent()) {
                response.sendError(404);
            } else {
                User existingUser = user.get();

                Result passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), existingUser.getPassword());    
                
                if (passwordVerify.verified) {
                    request.setAttribute("userId", existingUser.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
        
    }

   
    
}
