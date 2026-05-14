package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @PostMapping
    public ResponseEntity<Map<String, String>> criarUsuario(@RequestBody Usuario novoUsuario) {
        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Usuário cadastrado com sucesso!");

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }
}

class Usuario {
    private String nome;
    private String email;

    public Usuario() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
