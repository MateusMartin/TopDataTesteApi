package TopData.Api.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Objects;

import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class KeyUtils {
	
	@Autowired
	Environment enviroment;
	
	
	@Value("${access-token.private}")
	public String accessTokenPrivateKeyPath;
	

	@Value("${access-token.public}")
	public String accessTokenPublicKeyPath;
	

	@Value("${refresh-token.private}")
	public String refreshTokenPrivateKeyPath;
	

	@Value("${refresh-token.public}")
	public String refreshTokenPublicKeyPath;
	
	 private static Logger LOGGER = LoggerFactory.getLogger(SLF4JLogger.class);
	
	private KeyPair _accessTokenKeyPair;
	private KeyPair _refreshTokenKeyPair;
	
	private KeyPair getAcessTokenKeyPair() 
	{
		if(Objects.isNull(_accessTokenKeyPair)) 
		{
			_accessTokenKeyPair = getKeyPair(accessTokenPublicKeyPath,accessTokenPrivateKeyPath);
		}
		
		return _accessTokenKeyPair;
	}
	
	private KeyPair getRefreshTokenKeyPair() 
	{
		if(Objects.isNull(_refreshTokenKeyPair)) 
		{
			_refreshTokenKeyPair = getKeyPair(refreshTokenPublicKeyPath,refreshTokenPrivateKeyPath);
		}
		
		return _refreshTokenKeyPair;
	}
	
	
	private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath)  {
		
		KeyPair keyPair;
		
		File publicKeyFile = new File(publicKeyPath);
		File privateKeyFile = new File(privateKeyPath);
		
		
		if(publicKeyFile.exists() && privateKeyFile.exists()) 
		{
			LOGGER.info("loading keys from {}, {} ",publicKeyPath,privateKeyPath);

			try {
			
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			
			byte[] publicKeyBites = Files.readAllBytes(publicKeyFile.toPath());
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBites);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			
			
			byte[] privateyBites = Files.readAllBytes(privateKeyFile.toPath());
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateyBites);
			PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
			
			
			keyPair = new KeyPair(publicKey, privateKey);
			return keyPair;
			
			} catch(NoSuchAlgorithmException e) 
			{
				throw new RuntimeException(e);
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
			
		}else 
		{
			if(Arrays.stream(enviroment.getActiveProfiles()).anyMatch(s -> s.equals("prod")))
			{
				throw new RuntimeException("public and private keys don't exist");
			}
		}
		
		
		File directory = new File("access-refresh-token-keys");
		if(!directory.exists()) 
		{
			directory.mkdirs();
		}
		
		try 
		{
			LOGGER.info("Generating new public and private keys: {},{} ",publicKeyPath,privateKeyPath);
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
			
			try(FileOutputStream fos = new FileOutputStream(publicKeyPath))
			{
				X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
				fos.write(keySpec.getEncoded());
			}
			
			try(FileOutputStream fos = new FileOutputStream(privateKeyFile))
			{
				PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
				fos.write(keySpec.getEncoded());
				
			} 
			
		}catch(NoSuchAlgorithmException | IOException e) 
		{
			throw new RuntimeException(e);
		}
		
		return keyPair;
				
	}

	public RSAPublicKey getAcessTokenPublicKey() 
	{
		
		return (RSAPublicKey) getAcessTokenKeyPair().getPublic();
	}
	
	public RSAPrivateKey getAcessTokenPrivateKey() 
	{
		
		return (RSAPrivateKey) getAcessTokenKeyPair().getPrivate();
	}
	
	public RSAPublicKey getRefreshTokenPublicKey() 
	{
		
		return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
	}
	
	public RSAPrivateKey getRefreshTokenPrivateKey() 
	{
		return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
	}
	
}
