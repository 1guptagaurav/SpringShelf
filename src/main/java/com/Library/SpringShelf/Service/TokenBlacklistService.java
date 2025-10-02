package com.Library.SpringShelf.Service;


import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {

    // For a real-world app, replace this with a Redis or other distributed cache.
    private final Set<String> blacklist = Collections.synchronizedSet(new HashSet<>());

    public void blacklistToken(String token) {
        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
