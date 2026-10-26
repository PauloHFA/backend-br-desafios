package com.github.paulohfa.crypto.domain.model;

import com.github.paulohfa.crypto.domain.converter.EncryptedStringConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_operations")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "user_document", nullable = false, length = 512)
    private String userDocument;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "credit_card_token", nullable = false, length = 512)
    private String creditCardToken;

    @Column(name = "operation_value", nullable = false)
    private Long value;

    public Operation() {
    }

    public Operation(Long id, String userDocument, String creditCardToken, Long value) {
        this.id = id;
        this.userDocument = userDocument;
        this.creditCardToken = creditCardToken;
        this.value = value;
    }

    public Operation(String userDocument, String creditCardToken, Long value) {
        this.userDocument = userDocument;
        this.creditCardToken = creditCardToken;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserDocument() {
        return userDocument;
    }

    public void setUserDocument(String userDocument) {
        this.userDocument = userDocument;
    }

    public String getCreditCardToken() {
        return creditCardToken;
    }

    public void setCreditCardToken(String creditCardToken) {
        this.creditCardToken = creditCardToken;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
