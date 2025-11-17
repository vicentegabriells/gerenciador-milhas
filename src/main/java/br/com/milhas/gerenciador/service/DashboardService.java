package br.com.milhas.gerenciador.service;

import br.com.milhas.gerenciador.dto.AquisicaoResponseDTO;
import br.com.milhas.gerenciador.dto.PontosPorCartaoDTO;
import br.com.milhas.gerenciador.repository.CartaoRepository;
import com.itextpdf.kernel.pdf.PdfDocument; // 1. IMPORTAR PDF
import com.itextpdf.kernel.pdf.PdfWriter; // 2. IMPORTAR PDF
import com.itextpdf.layout.Document; // 3. IMPORTAR PDF
import com.itextpdf.layout.element.Cell; // 4. IMPORTAR PDF
import com.itextpdf.layout.element.Paragraph; // 5. IMPORTAR PDF
import com.itextpdf.layout.element.Table; // 6. IMPORTAR PDF
import com.itextpdf.layout.properties.UnitValue; // 7. IMPORTAR PDF
import jakarta.servlet.http.HttpServletResponse; // 8. IMPORTAR
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream; // 9. IMPORTAR (PDF usa OutputStream, não Writer)
import java.io.Writer;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private AquisicaoService aquisicaoService;

    // --- Método que já existia ---
    public List<PontosPorCartaoDTO> getPontosPorCartao(String emailUsuarioLogado) {
        return cartaoRepository.findPontosPorCartaoByUsuarioEmail(emailUsuarioLogado);
    }

    // --- Método que já existia ---
    public void escreverHistoricoAquisicoesCSV(Writer writer, String emailUsuarioLogado) throws IOException {
        List<AquisicaoResponseDTO> historico = aquisicaoService.listarPorUsuario(emailUsuarioLogado);
        String[] headers = {
            "ID Aquisicao", "Descricao", "Valor Gasto", "Pontos Calculados",
            "Data Compra", "Data Prevista Credito", "Status",
            "ID Cartao", "Nome Cartao"
        };

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .build());

        for (AquisicaoResponseDTO aquisicao : historico) {
            csvPrinter.printRecord(
                aquisicao.id(), aquisicao.descricao(), aquisicao.valorGasto(),
                aquisicao.pontosCalculados(), aquisicao.dataCompra(), aquisicao.dataPrevistaCredito(),
                aquisicao.status(), aquisicao.cartaoId(), aquisicao.nomeCartao()
            );
        }
        csvPrinter.flush();
        csvPrinter.close();
    }

    // --- 10. NOVO MÉTODO ADICIONADO ---
    /**
     * Escreve o histórico de aquisições do usuário em um documento PDF.
     *
     * @param response A resposta HTTP para onde o PDF será escrito.
     * @param emailUsuarioLogado O e-mail do usuário para filtrar os dados.
     */
    public void escreverHistoricoAquisicoesPDF(HttpServletResponse response, String emailUsuarioLogado) throws IOException {
        
        // 1. Busca os dados
        List<AquisicaoResponseDTO> historico = aquisicaoService.listarPorUsuario(emailUsuarioLogado);

        // 2. Inicializa o escritor de PDF
        // Pegamos o OutputStream da resposta, pois PDF é um fluxo de bytes
        OutputStream outputStream = response.getOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // 3. Adiciona um Título ao PDF
        document.add(new Paragraph("Historico de Aquisicoes de Pontos"));

        // 4. Define as colunas da tabela
        float[] columnWidths = {1, 3, 2, 2, 2, 2, 2, 1, 3}; // Larguras relativas
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100)); // Tabela usa 100% da largura

        // 5. Adiciona os Cabeçalhos da Tabela
        table.addHeaderCell(new Cell().add(new Paragraph("ID")));
        table.addHeaderCell(new Cell().add(new Paragraph("Descricao")));
        table.addHeaderCell(new Cell().add(new Paragraph("Valor Gasto")));
        table.addHeaderCell(new Cell().add(new Paragraph("Pontos")));
        table.addHeaderCell(new Cell().add(new Paragraph("Data Compra")));
        table.addHeaderCell(new Cell().add(new Paragraph("Data Credito")));
        table.addHeaderCell(new Cell().add(new Paragraph("Status")));
        table.addHeaderCell(new Cell().add(new Paragraph("Cartao ID")));
        table.addHeaderCell(new Cell().add(new Paragraph("Nome Cartao")));

        // 6. Adiciona os Dados (linhas) da Tabela
        for (AquisicaoResponseDTO aquisicao : historico) {
            table.addCell(new Cell().add(new Paragraph(aquisicao.id().toString())));
            table.addCell(new Cell().add(new Paragraph(aquisicao.descricao())));
            table.addCell(new Cell().add(new Paragraph(aquisicao.valorGasto().toString())));
            table.addCell(new Cell().add(new Paragraph(aquisicao.pontosCalculados().toString())));
            table.addCell(new Cell().add(new Paragraph(aquisicao.dataCompra().toString())));
            table.addCell(new Cell().add(new Paragraph(aquisicao.dataPrevistaCredito().toString())));
            table.addCell(new Cell().add(new Paragraph(aquisicao.status())));
            table.addCell(new Cell().add(new Paragraph(aquisicao.cartaoId().toString())));
            table.addCell(new Cell().add(new Paragraph(aquisicao.nomeCartao())));
        }

        // 7. Adiciona a tabela ao documento
        document.add(table);

        // 8. Fecha o documento (isso finaliza o arquivo e o envia)
        document.close();
        outputStream.flush();
        outputStream.close();
    }
}