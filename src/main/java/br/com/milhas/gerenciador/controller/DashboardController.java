package br.com.milhas.gerenciador.controller;

import br.com.milhas.gerenciador.dto.PontosPorCartaoDTO;
import br.com.milhas.gerenciador.service.DashboardService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // --- Método que já existia ---
    @GetMapping("/pontos-por-cartao")
    public ResponseEntity<List<PontosPorCartaoDTO>> getPontosPorCartao(Authentication authentication) {
        String emailUsuarioLogado = authentication.getName();
        List<PontosPorCartaoDTO> dados = dashboardService.getPontosPorCartao(emailUsuarioLogado);
        return ResponseEntity.ok(dados);
    }

    // --- Método que já existia ---
    @GetMapping("/exportar-historico-csv")
    public void exportarHistoricoCSV(HttpServletResponse response, Authentication authentication) throws IOException {
        String emailUsuarioLogado = authentication.getName();
        response.setContentType("text/csv");
        String dataFormatada = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String nomeArquivo = "historico_aquisicoes_" + dataFormatada + ".csv";
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");
        dashboardService.escreverHistoricoAquisicoesCSV(response.getWriter(), emailUsuarioLogado);
    }

    // --- 1. NOVO MÉTODO ADICIONADO ---
    /**
     * Endpoint para exportar o histórico de aquisições em formato PDF.
     * Acessível via: GET /api/dashboard/exportar-historico-pdf
     * Requer autenticação (Token JWT).
     */
    @GetMapping("/exportar-historico-pdf")
    public void exportarHistoricoPDF(HttpServletResponse response, Authentication authentication) throws IOException {
        
        // 1. Pega o e-mail do usuário logado
        String emailUsuarioLogado = authentication.getName();

        // 2. Define o tipo de conteúdo da resposta como "application/pdf"
        response.setContentType("application/pdf");

        // 3. Cria um nome de arquivo dinâmico (ex: "historico_2025-11-16.pdf")
        String dataFormatada = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String nomeArquivo = "historico_aquisicoes_" + dataFormatada + ".pdf";

        // 4. Define o cabeçalho "Content-Disposition" para forçar o download
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");

        // 5. Chama o serviço para escrever o PDF diretamente na resposta HTTP
        dashboardService.escreverHistoricoAquisicoesPDF(response, emailUsuarioLogado);
    }
}