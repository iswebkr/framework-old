package kr.isweb.cmmn.config.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import kr.isweb.cmmn.config.jwt.dto.CmmnJwtToken;

@Component
public class CmmnJwtTokenProvider {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${jwt.access.token.secret}")
	private String jwtAccessTokenSecret;

	@Value("${jwt.refresh.token.secret}")
	private String jwtRefreshTokenSecret;

	// 1시간동안 AccessToken 활성화
	private long jwtAccessTokenExpirationTime = 1000 * 60 * 60;

	// 1일 동안 refreshToken 활성화
	private long jwtRefreshTokenExpirationTime = 1000 * 60 * 60 * 24 * 1;

	public enum GRANT_TYPE {
		ACCESS_TOKEN,
		REFRESH_TOKEN
	}

	private class grantTypeData {
		private String secretKey;
		private long expirationTime = 0;

		public grantTypeData(GRANT_TYPE grantType) {
			if(grantType == GRANT_TYPE.ACCESS_TOKEN) {
				this.secretKey = Base64.encodeBase64String(jwtAccessTokenSecret.getBytes());
				this.expirationTime = jwtAccessTokenExpirationTime;
			} else if (grantType == GRANT_TYPE.REFRESH_TOKEN) {
				this.secretKey = Base64.encodeBase64String(jwtRefreshTokenSecret.getBytes());
				this.expirationTime = jwtRefreshTokenExpirationTime;
			}
		}

		public String getSecretKey() {
			return secretKey;
		}

		public long getExpirationTime() {
			return expirationTime;
		}
	}

	public grantTypeData getGrantTypeData(GRANT_TYPE grantType) {
		grantTypeData tokenDataType = null;
		tokenDataType = new grantTypeData(grantType);
		return tokenDataType;
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver, GRANT_TYPE grantType) {
		grantTypeData ttd = getGrantTypeData(grantType);
		Claims claims = _getAllClaimsFromToken(token, ttd);
		return claimsResolver.apply(claims);
	}

	public Date getExpirationDateFromToken(String token, GRANT_TYPE grantType) {
		Date date = null;
		try {
			date = getClaimFromToken(token, Claims::getExpiration, grantType);
		} catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
		return date;
	}

	public CmmnJwtToken makeToken(String id) {
		return generateToken(id, new HashMap<>());
	}

	public CmmnJwtToken makeRefreshToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return generateToken(authentication.getName(),  new HashMap<>());
	}

	public CmmnJwtToken generateToken(String id, Map<String, Object> claims) {
		String accessToken = _doGenerateToken(id, claims, GRANT_TYPE.ACCESS_TOKEN);
		String refreshToken = _doGenerateToken(id, claims, GRANT_TYPE.REFRESH_TOKEN);
		return new CmmnJwtToken(accessToken, refreshToken);
	}

	public String getUsernameFromJwtToken(String token, GRANT_TYPE grantType) {
		String username = null;
		try {
			username = getClaimFromToken(token, Claims::getId, grantType);
		} catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
		return username;
	}

	public boolean validateJwtToken(String token, UserDetails userDetails, GRANT_TYPE grantType) {
		String username = getUsernameFromJwtToken(token, grantType);
		return username.equals(userDetails.getUsername()) && !_isTokenExpired(token, grantType);
	}

	private Claims _getAllClaimsFromToken(@Nonnull String token, @Nonnull grantTypeData ttd) {
		return Jwts.parser().setSigningKey(ttd.getSecretKey()).parseClaimsJws(token).getBody();
	}

	private boolean _isTokenExpired(String token, GRANT_TYPE grantType) {
		Date expiration = getExpirationDateFromToken(token, grantType);
		return expiration.before(new Date());
	}

	private String _doGenerateToken(String id, Map<String, Object> claims, GRANT_TYPE grantType) {
		grantTypeData ttd = getGrantTypeData(grantType);

		Date now = new Date(System.currentTimeMillis());
		Date tokenExpiration = new Date(now.getTime() + ttd.getExpirationTime());

		return Jwts.builder()
				.setClaims(claims)
				.setId(id)
				.setIssuedAt(now)
				.setExpiration(tokenExpiration)
				.signWith(SignatureAlgorithm.HS256, ttd.getSecretKey())
				.compact();
	}
}
