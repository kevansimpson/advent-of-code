package org.base.advent.util;

import java.security.MessageDigest;

import static org.base.advent.util.HashAtIndex.convertToMD5ThenHex;
import static org.base.advent.util.HashCache.newMD5;

/**
 * Simple utility to not have code littered with {@link HashCache#newMD5()} and {@link MessageDigest#reset()}.
 */
public class ReusableDigest {
    private final MessageDigest digest = newMD5();

    public String hashHex(String input) {
        try {
            return convertToMD5ThenHex(digest, input);
        }
        finally {
            digest.reset();
        }
    }
}
