package fr.glowingenigma.domain.common.security;

public interface PasswordEncryptor {

    /**
     * Encrypt a raw password
     */
    String encrypt(String rawPassword);
}
