package com.github.paulohfa.crypto.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Servico responsavel pela criptografia e descriptografia simetrica utilizando AES-256 no modo GCM.
 * O modo GCM (Galois/Counter Mode) fornece criptografia autenticada (AEAD), garantindo sigilo e integridade.
 */
@Service
public class CryptoService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH_BYTES = 12; // 96 bits recomendado para GCM
    private static final int GCM_TAG_LENGTH_BITS = 128; // 128 bits de tag de autenticacao

    private final SecretKey secretKey;
    private final SecureRandom secureRandom;

    public CryptoService(@Value("${app.security.crypto-secret}") String base64SecretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(base64SecretKey);
        this.secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
        this.secureRandom = new SecureRandom();
    }

    /**
     * Criptografa o texto legivel.
     * O IV gerado e anexado aos primeiros 12 bytes do payload resultante antes da codificacao Base64.
     *
     * @param plainText texto a ser criptografado
     * @return texto cifrado em Base64 ou null se o parametro for nulo
     */
    public String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }

        try {
            byte[] iv = new byte[GCM_IV_LENGTH_BYTES];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Combina IV (12 bytes) + CipherText em um unico array de bytes
            byte[] combined = ByteBuffer.allocate(iv.length + cipherText.length)
                    .put(iv)
                    .put(cipherText)
                    .array();

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception ex) {
            throw new CryptographyException("Falha ao criptografar dado sensivel", ex);
        }
    }

    /**
     * Descriptografa o texto cifrado em Base64.
     *
     * @param encryptedText texto cifrado em Base64
     * @return texto original decifrado ou null se o parametro for nulo
     */
    public String decrypt(String encryptedText) {
        if (encryptedText == null) {
            return null;
        }

        try {
            byte[] combined = Base64.getDecoder().decode(encryptedText);

            if (combined.length < GCM_IV_LENGTH_BYTES) {
                throw new IllegalArgumentException("Formato de dado cifrado invalido.");
            }

            ByteBuffer buffer = ByteBuffer.wrap(combined);
            byte[] iv = new byte[GCM_IV_LENGTH_BYTES];
            buffer.get(iv);

            byte[] cipherText = new byte[buffer.remaining()];
            buffer.get(cipherText);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] plainTextBytes = cipher.doFinal(cipherText);
            return new String(plainTextBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new CryptographyException("Falha ao descriptografar dado sensivel", ex);
        }
    }
}
