package com.github.paulohfa.crypto.domain.converter;

import com.github.paulohfa.crypto.security.CryptoService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Conversor JPA transparente para campos criptografados.
 * No momento da gravacao (convertToDatabaseColumn), cifra o dado antes de enviar ao banco.
 * No momento da leitura (convertToEntityAttribute), decifra o dado ao mapear para a entidade.
 */
@Converter
@Component
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    private static CryptoService staticCryptoService;

    @Autowired
    public void setCryptoService(CryptoService cryptoService) {
        EncryptedStringConverter.staticCryptoService = cryptoService;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        if (staticCryptoService == null) {
            throw new IllegalStateException("CryptoService nao foi inicializado no EncryptedStringConverter");
        }
        return staticCryptoService.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        if (staticCryptoService == null) {
            throw new IllegalStateException("CryptoService nao foi inicializado no EncryptedStringConverter");
        }
        return staticCryptoService.decrypt(dbData);
    }
}

