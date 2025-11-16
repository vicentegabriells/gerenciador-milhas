package br.com.milhas.gerenciador.service;

import br.com.milhas.gerenciador.dto.AquisicaoCadastroDTO;
import br.com.milhas.gerenciador.dto.AquisicaoResponseDTO;
import br.com.milhas.gerenciador.model.Aquisicao;
import br.com.milhas.gerenciador.model.Cartao;
import br.com.milhas.gerenciador.model.StatusCredito;
import br.com.milhas.gerenciador.repository.AquisicaoRepository;
import br.com.milhas.gerenciador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AquisicaoService {

    @Autowired
    private AquisicaoRepository aquisicaoRepository;
    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private FileStorageService fileStorageService;

    // @Transactional garante que, se o upload do arquivo falhar,
    // o registro no banco será desfeito (rollback).
    @Transactional
    public AquisicaoResponseDTO registrarAquisicao(AquisicaoCadastroDTO dto, MultipartFile comprovante, String emailUsuarioLogado) {

        // 1. Valida se o cartão existe e pertence ao usuário logado
        Cartao cartao = cartaoRepository.findById(dto.cartaoId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));
        
        if (!cartao.getUsuario().getEmail().equals(emailUsuarioLogado)) {
            throw new RuntimeException("Este cartão não pertence ao usuário logado.");
        }

        // 2. Calcula os pontos automaticamente 
        BigDecimal pontosCalculados = dto.valorGasto().multiply(cartao.getFatorConversao());

        // 3. Cria a entidade e salva no banco (1ª vez) para gerar o ID
        Aquisicao novaAquisicao = new Aquisicao();
        novaAquisicao.setDescricao(dto.descricao());
        novaAquisicao.setValorGasto(dto.valorGasto());
        novaAquisicao.setDataCompra(dto.dataCompra());
        novaAquisicao.setDataPrevistaCredito(dto.dataPrevistaCredito());
        novaAquisicao.setCartao(cartao);
        novaAquisicao.setPontosCalculados(pontosCalculados);
        novaAquisicao.setStatus(StatusCredito.PENDENTE);
        
        Aquisicao aquisicaoSalva = aquisicaoRepository.save(novaAquisicao);

        // 4. Salva o arquivo no disco usando o ID gerado
        String nomeDoArquivo = fileStorageService.storeFile(comprovante, aquisicaoSalva.getId());

        // 5. Atualiza a entidade com o nome do arquivo (2ª vez)
        aquisicaoSalva.setCaminhoComprovante(nomeDoArquivo);
        Aquisicao aquisicaoFinal = aquisicaoRepository.save(aquisicaoSalva);

        // 6. Retorna o DTO de resposta
        return new AquisicaoResponseDTO(aquisicaoFinal);
    }

    /**
     * Lista todas as aquisições do usuário logado.
     */
    public List<AquisicaoResponseDTO> listarPorUsuario(String emailUsuarioLogado) {
        // Usa o método customizado do repositório
        List<Aquisicao> aquisicoes = aquisicaoRepository.findByCartaoUsuarioEmail(emailUsuarioLogado);

        // Converte a lista de Entidades para uma lista de DTOs
        return aquisicoes.stream()
                .map(AquisicaoResponseDTO::new)
                .collect(Collectors.toList());
    }
}
