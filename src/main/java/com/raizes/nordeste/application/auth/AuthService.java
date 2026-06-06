package com.raizes.nordeste.application.auth;


import com.raizes.nordeste.api.dto.request.CriarUsuarioRequest;
import com.raizes.nordeste.api.dto.response.LoginResponse;
import com.raizes.nordeste.api.dto.response.UsuarioResponse;
import com.raizes.nordeste.api.exception.EmailJaCadastradoException;
import com.raizes.nordeste.api.exception.RecursoNaoEncontradoException;
import com.raizes.nordeste.domain.model.Usuario;
import com.raizes.nordeste.infrastructure.repository.UsuarioRepository;
import com.raizes.nordeste.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponse login(String email, String senha) {
        //delega a verificação das credenciais para o Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha));

        Usuario usuario = usuarioRepository
                .findByEmailAndAtivoTrue(email)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuario", 0L));

        String token = jwtUtil.gerarToken(usuario);

        return new LoginResponse(
                token,
                "Bearer",
                3600L,
                new LoginResponse.UsuarioInfo(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getPerfil().name()));
    }

    @Transactional
    public UsuarioResponse cadastrar(CriarUsuarioRequest req) {

        if (usuarioRepository.existsByEmail(req.email())) {
            throw new EmailJaCadastradoException(req.email());
        }

        Usuario usuario = new Usuario();
        usuario.setNome(req.nome());
        usuario.setEmail(req.email());
        usuario.setSenhaHash(
                passwordEncoder.encode(req.senha()));
        usuario.setPerfil(req.perfil());
        usuario.setConsentimentoLgpd(req.consentimentoLgpd());
        usuarioRepository.save(usuario);

        return UsuarioResponse.from(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPerfil(String email) {
        return usuarioRepository
                .findByEmailAndAtivoTrue(email)
                .map(UsuarioResponse::from)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuario", 0L));
    }
}