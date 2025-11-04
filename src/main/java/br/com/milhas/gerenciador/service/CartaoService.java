package br.com.milhas.gerenciador.service;

import br.com.milhas.gerenciador.dto.CartaoCadastroDTO;
import br.com.milhas.gerenciador.dto.CartaoResponseDTO;
import br.com.milhas.gerenciador.model.Bandeira;
import br.com.milhas.gerenciador.model.Cartao;
import br.com.milhas.gerenciador.model.ProgramaDePontos;
import br.com.milhas.gerenciador.model.Usuario;
import br.com.milhas.gerenciador.repository.BandeiraRepository;
import br.com.milhas.gerenciador.repository.CartaoRepository;
import br.com.milhas.gerenciador.repository.ProgramaDePontosRepository;
import br.com.milhas.gerenciador.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartaoService {

    // 1. Injeta todos os repositórios que vamos precisar
    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BandeiraRepository bandeiraRepository;
    @Autowired
    private ProgramaDePontosRepository programaRepository;

    /**
     * Cadastra um novo cartão para o usuário logado.
     * @param dto Os dados do cartão (nome, saldo, bandeiraId, programaId).
     * @param emailUsuarioLogado O e-mail do usuário vindo do token JWT.
     * @return O cartão salvo, convertido para CartaoResponseDTO.
     */
    public CartaoResponseDTO cadastrar(CartaoCadastroDTO dto, String emailUsuarioLogado) {
        // 2. Busca as entidades relacionadas no banco de dados
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Bandeira bandeira = bandeiraRepository.findById(dto.bandeiraId())
                .orElseThrow(() -> new RuntimeException("Bandeira não encontrada"));

        ProgramaDePontos programa = programaRepository.findById(dto.programaId())
                .orElseThrow(() -> new RuntimeException("Programa de pontos não encontrado"));

        // 3. Cria a nova entidade Cartao
        Cartao novoCartao = new Cartao();
        novoCartao.setNome(dto.nome());
        novoCartao.setSaldoDePontos(dto.saldoDePontos());
        novoCartao.setUsuario(usuario); // Associa ao usuário logado
        novoCartao.setBandeira(bandeira); // Associa à bandeira
        novoCartao.setProgramaDePontos(programa); // Associa ao programa

        // 4. Salva o novo cartão no banco
        Cartao cartaoSalvo = cartaoRepository.save(novoCartao);

        // 5. Retorna o DTO de resposta
        return new CartaoResponseDTO(cartaoSalvo);
    }

    /**
     * Lista todos os cartões pertencentes ao usuário logado.
     * @param emailUsuarioLogado O e-mail do usuário vindo do token JWT.
     * @return Uma lista de CartaoResponseDTO.
     */
    public List<CartaoResponseDTO> listarPorUsuario(String emailUsuarioLogado) {
        // 1. Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Busca os cartões usando o método que criamos no CartaoRepository
        List<Cartao> cartoesDoUsuario = cartaoRepository.findByUsuarioId(usuario.getId());

        // 3. Converte a lista de Entidades (Cartao) para uma lista de DTOs (CartaoResponseDTO)
        return cartoesDoUsuario.stream()
                .map(CartaoResponseDTO::new) // Usa o construtor do DTO
                .collect(Collectors.toList());
    }
}
