package com.github.paulohfa.crypto.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class CryptoServiceTest {

    private CryptoService cryptoService;

    @BeforeEach
    void setUp() {
        // Chave AES de 256 bits codificada em Base64 (32 bytes)
        byte[] key32Bytes = "12345678901234567890123456789012".getBytes();
        String base64Key = Base64.getEncoder().encodeToString(key32Bytes);
        cryptoService = new CryptoService(base64Key);
    }

    @Test
    @DisplayName("Deve criptografar e descriptografar retornando o texto original")
    void shouldEncryptAndDecryptSuccessfully() {
        String original = "123.456.789-00";

        String encrypted = cryptoService.encrypt(original);
        assertNotNull(encrypted);
        assertNotEquals(original, encrypted);

        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(original, decrypted);
    }

    @Test
    @DisplayName("Deve gerar cifras diferentes para o mesmo texto devido ao IV aleatorio (GCM)")
    void shouldGenerateDifferentCiphertextForSameInput() {
        String plain = "token_cartao_secreto_999";

        String cipher1 = cryptoService.encrypt(plain);
        String cipher2 = cryptoService.encrypt(plain);

        assertNotEquals(cipher1, cipher2);
        assertEquals(plain, cryptoService.decrypt(cipher1));
        assertEquals(plain, cryptoService.decrypt(cipher2));
    }

    @Test
    @DisplayName("Deve retornar null quando a entrada for null")
    void shouldReturnNullForNullInput() {
        assertNull(cryptoService.encrypt(null));
        assertNull(cryptoService.decrypt(null));
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar decifrar dado corrompido")
    void shouldThrowExceptionWhenCiphertextIsInvalid() {
        assertThrows(CryptographyException.class, () -> cryptoService.decrypt("dado-invalido-base64"));
    }
}

