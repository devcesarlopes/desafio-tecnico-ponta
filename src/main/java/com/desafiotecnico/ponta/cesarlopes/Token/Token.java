package com.desafiotecnico.ponta.cesarlopes.Token;

import com.desafiotecnico.ponta.cesarlopes.Usuarios.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

public class Token {
    private static final String SECRET_KEY = "7bfaf6547d528cf529808908360864759f2273ae21cde1c22b0a0f8a4a6a152a1c03138909fcc66e85b997bd6de229e598a34ecdb62c1f84c22a1be892c2ad3bb3aab38b02ffa9f69403efa12272f5f3d3764cd2694d5117ddc573c11d7b4cf2aaca62fe003401e8121579d6728a40d6e69c5388e3f6da06693a3fc2dc082f7154c2e22f249471bff936807681ea2b46779a49bb889a8e39be0d3dd273104cd8b3be081d22ce204ecb5848aa42bcc4b77b43643ddee5a4b0342dfff9f7bbadd87313be23775996bc00e7ec31553208b2558ed2fa6caba18f4f7229dcf098d5a24e8139a121c6570feb83b596914632db69f5861382397ffee935c17033cda8a0"; // Same secret key used in UserController

    public static String obterSecretKey() {
        return SECRET_KEY;
    }

    public static UUID obterIdDeUsuarioAtravesDoToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization token missing or invalid");
        }

        String token = header.substring(7);
        Claims claims = Jwts.parser()
                .setSigningKey(Token.obterSecretKey())
                .parseClaimsJws(token)
                .getBody();
        return UUID.fromString(claims.getSubject());
    }

    public static String gerarToken(User usuario) {
        long expirationTime = 1000 * 60 * 60 * 24; // 24 hours in milliseconds
        return Jwts.builder()
                .setSubject(usuario.getId().toString())
                .claim("name", usuario.getName())
                .claim("login", usuario.getLogin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, Token.obterSecretKey())
                .compact();
    }

}
