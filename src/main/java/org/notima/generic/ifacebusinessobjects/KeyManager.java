package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.PublicKey;

/**
 * A service template that manages pgp keys and allows key look up by email address.
 * 
 * @author Oliver Norin
 *
 */
public interface KeyManager {

    public PublicKey add(PublicKey key);
    public PublicKey update(PublicKey key);
    public PublicKey remove(PublicKey key);
    public PublicKey get(String userId);

    /**
     * List all stored keys
     * @return
     */
    public List<PublicKey> list();
}
