package br.com.milhas.gerenciador.controller;

import br.com.milhas.gerenciador.dto.PontosPorCartaoDTO;
import br.com.milhas.gerenciador.dto.PrazoMedioDTO; // 1. IMPORTAR
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

    // --- 2. NOVO MÉTODO ADICIONADO ---
    /**
     * Endpoint para o KPI (indicador) de "Prazo Médio de Recebimento".
     * Acessível via: GET /api/dashboard/prazo-medio-recebimento
     * Requer autenticação (Token JWT).
     */
    @GetMapping("/prazo-medio-recebimento")
    public ResponseEntity<PrazoMedioDTO> getPrazoMedioRecebimento(Authentication authentication) {
        String emailUsuarioLogado = authentication.getName();
        
        // Chama o serviço para obter o DTO com o cálculo
        PrazoMedioDTO prazoMedio = dashboardService.getPrazoMedioRecebimento(emailUsuarioLogado);
        
        return ResponseEntity.ok(prazoMedio);
    }

    // --- Métodos que já existiam (CSV e PDF) ---
    @GetMapping("/exportar-historico-csv")
    public void exportarHistoricoCSV(HttpServletResponse response, Authentication authentication) throws IOException {
        String emailUsuarioLogado = authentication.getName();
        response.setContentType("text/csv");
        String dataFormatada = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String nomeArquivo = "historico_aquisicoes_" + dataFormatada + ".csv";
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");
        dashboardService.escreverHistoricoAquisicoesCSV(response.getWriter(), emailUsuarioLogado);
    }

    @GetMapping("/exportar-historico-pdf")
    public void exportarHistoricoPDF(HttpServletResponse response, Authentication authentication) throws IOException {
        String emailUsuarioLogado = authentication.getName();
        response.setContentType("application/pdf");
        String dataFormatada = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String nomeArquivo = "historico_aquisicoes_" + dataFormatada + ".pdf";
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");
        dashboardService.escreverHistoricoAquisicoesPDF(response, emailUsuarioLogado);
    }
}