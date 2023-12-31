package com.rit.starterboot.servlet.configuration.jwt;

import com.rit.robusta.util.KeyStores;
import com.rit.starterboot.servlet.configuration.jwt.properties.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class JwtKeyStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtKeyStore.class);

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtKeyStore(JwtProperties jwtProperties) {
        var keystore = readKeyStore(jwtProperties);
        privateKey = readRsaPrivateKey(keystore, jwtProperties);
        publicKey = readRsaPublicKey(keystore, jwtProperties);
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    private static KeyStore readKeyStore(JwtProperties jwtProperties) {
        try {
            return KeyStores.readKeyStore(jwtProperties.keyStorePath(), jwtProperties.keyStorePasswordAsArray());
        } catch (KeyStoreException ex) {
            LOGGER.error("Unable to load keystore: {}", jwtProperties.keyStorePath(), ex);
        }
        throw new IllegalArgumentException("Unable to load keystore");
    }

    private static RSAPrivateKey readRsaPrivateKey(KeyStore keyStore, JwtProperties jwtProperties) {
        try {
            Key key = keyStore.getKey(jwtProperties.keyAlias(), jwtProperties.privateKeyPassphraseAsArray());
            if (key instanceof RSAPrivateKey) {
                return (RSAPrivateKey) key;
            }
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException ex) {
            LOGGER.error("Unable to load private key from keystore: {}", jwtProperties.keyStorePath(), ex);
        }

        throw new IllegalArgumentException("Unable to load private key");
    }

    private static RSAPublicKey readRsaPublicKey(KeyStore keyStore, JwtProperties jwtProperties) {
        try {
            Certificate certificate = keyStore.getCertificate(jwtProperties.keyAlias());
            PublicKey publicKey = certificate.getPublicKey();
            if (publicKey instanceof RSAPublicKey) {
                return (RSAPublicKey) publicKey;
            }
        } catch (KeyStoreException ex) {
            LOGGER.error("Unable to load private key from keystore: {}", jwtProperties.keyStorePath(), ex);
        }

        throw new IllegalArgumentException("Unable to load RSA public key");
    }
}
