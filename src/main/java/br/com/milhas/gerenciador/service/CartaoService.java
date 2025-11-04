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
     */
    public CartaoResponseDTO cadastrar(CartaoCadastroDTO dto, String emailUsuarioLogado) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Bandeira bandeira = bandeiraRepository.findById(dto.bandeiraId())
                .orElseThrow(() -> new RuntimeException("Bandeira não encontrada"));

        ProgramaDePontos programa = programaRepository.findById(dto.programaId())
                .orElseThrow(() -> new RuntimeException("Programa de pontos não encontrado"));

        Cartao novoCartao = new Cartao();
        novoCartao.setNome(dto.nome());
        novoCartao.setSaldoDePontos(dto.saldoDePontos());
        novoCartao.setFatorConversao(dto.fatorConversao()); // --- LINHA ADICIONADA ---
        novoCartao.setUsuario(usuario);
        novoCartao.setBandeira(bandeira);
        novoCartao.setProgramaDePontos(programa);

        Cartao cartaoSalvo = cartaoRepository.save(novoCartao);
        
        return new CartaoResponseDTO(cartaoSalvo);
    }

    /**
     * Lista todos os cartões pertencentes ao usuário logado.
     */
    public List<CartaoResponseDTO> listarPorUsuario(String emailUsuarioLogado) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Cartao> cartoesDoUsuario = cartaoRepository.findByUsuarioId(usuario.getId());

        // O mapeamento aqui já funciona por causa da nossa atualização no CartaoResponseDTO
        return cartoesDoUsuario.stream()
                .map(CartaoResponseDTO::new)
                .collect(Collectors.toList());
    }
}