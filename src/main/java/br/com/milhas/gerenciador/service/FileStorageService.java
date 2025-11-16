package br.com.milhas.gerenciador.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    
    private final Path fileStorageLocation;

    // Construtor que lê a propriedade 'file.upload-dir' e cria o diretório
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o diretório para upload de arquivos.", ex);
        }
    }

    /**
     * Salva o arquivo no sistema de arquivos.
     * @param file O arquivo enviado na requisição.
     * @param aquisicaoId O ID da aquisição, para criar um nome de arquivo único.
     * @return O nome do arquivo salvo.
     */
    public String storeFile(MultipartFile file, Long aquisicaoId) {
        // Limpa o nome do arquivo (ex: remove "C:\temp\...")
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";

        try {
            // Pega a extensão (ex: ".pdf")
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (Exception e) {
            // Caso o arquivo não tenha extensão
            fileExtension = "";
        }

        // Cria um nome de arquivo único para evitar conflitos
        String newFileName = "aquisicao-" + aquisicaoId + "-" + System.currentTimeMillis() + fileExtension;

        try {
            // Validação de segurança
            if(newFileName.contains("..")) {
                throw new RuntimeException("Nome de arquivo inválido: " + newFileName);
            }

            // Define o caminho final do arquivo
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            
            // Copia o arquivo da requisição para o diretório de destino
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return newFileName; // Retorna apenas o nome do arquivo
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível salvar o arquivo " + newFileName, ex);
        }
    }
}
