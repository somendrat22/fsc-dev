package com.fsc.supply_chain_common_operations.utilities;

import com.fsc.supply_chain_common_operations.constants.SystemConstant;
import com.fsc.supply_chain_common_operations.models.Role;
import com.fsc.supply_chain_common_operations.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtility {

    public final Key key = Keys.hmacShaKeyFor(SystemConstant.JWT_SECRET_PASSWORD.getBytes());

    public String generateJwtToken(User user){
        // Information which we want to encrypt inside the token.
        String userEmail = user.getEmailAddress(); // CLAIM1

        List<String> roleNames = user.getRoles().stream().map(Role::getRoleName).toList(); // CLAIM2

        // Whatever userInformation we encrypt inside the token we call it as claims.
        HashMap<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("email" , userEmail);
        claimsMap.put("roles", roleNames);

        // Generating token
        return Jwts.builder()
                .addClaims(claimsMap)
                .setExpiration(new Date(System.currentTimeMillis() + SystemConstant.JWT_TOKEN_EXPIRATION_TIME_IN_MILLIS))
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();
    }




    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
