package com.atschx.adnetwork.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atschx.adnetwork.domain.model.Token;
import com.atschx.adnetwork.domain.model.User;
import com.atschx.adnetwork.domain.repository.TokenRepository;
import com.atschx.adnetwork.domain.repository.UserRepository;
import com.atschx.adnetwork.protocol.response.SigninResult;

@Service
public class SigninService {

	private Logger logger = LoggerFactory.getLogger(SigninService.class);

	private final TokenRepository tokenRepository;
	private final UserRepository userRepository;

	@Autowired
	public SigninService(TokenRepository tokenRepository, UserRepository userRepository) {
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
	}

	public SigninResult signin(String email, String password) {

		if (logger.isDebugEnabled()) {
			logger.debug("用户{}使用密码{}登录!", email, password);
		}

		User user = userRepository.findByEmail(email);
		if (user != null) {
			if (user.getPassword().equals(password)) {
				final String token = UUID.randomUUID().toString().replaceAll("-", "");
				final long expires = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
				tokenRepository.save(new Token(token, user.getId(), expires));
				return new SigninResult(token, expires);
			}
		}

		return new SigninResult("1");
	}

}