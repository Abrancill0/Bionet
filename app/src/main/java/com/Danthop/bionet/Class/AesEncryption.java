package com.Danthop.bionet.Class;

import com.serti.pandora.crypto.EncryptionException;

import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec; import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import static com.google.android.gms.common.util.Base64Utils.encode;
import static org.bouncycastle.util.encoders.UrlBase64.decode;

public class AesEncryption implements SymmetricEcryptionComponent {

    Map<String,String> initParams;
    String key, iv;
    String mode, encoding;
    String keyFile;
    String ENCODING = "ISO-8859-1";

    public AesEncryption()
    {
        Security.addProvider( new BouncyCastleProvider( ) );
    }

    public void setInitParams() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        initParams = map;

        key="1ea9a91b0ba908b44f598d2822499441";
        iv= "f20946931dd6e8594dc6f469b5e583ab";
        mode= "AES/CBC/PKCS7Padding";
        encoding= "HEX";

        if(encoding.equalsIgnoreCase("BASE64")&&encoding.equalsIgnoreCase("HEX"))
            throw new IllegalArgumentException("AES.ENCODING can only be 'HEX' of 'BASE64'");
    }

    /* CIFRADO DE LA INFORMACIÓN @return encodeBase24 **/ public String encrypt(String data) {

        byte[] output = null; try {
            byte[] keyBytes = decode(key);
            byte[] input = data.getBytes(ENCODING);
            AlgorithmParameterSpec ivSpec= new IvParameterSpec(Hex.decodeHex(iv.toCharArray())); SecretKeySpec keySpec = null;
            keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(mode);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            output = cipher.doFinal(input);
        }
        catch(Exception e){
            throw new EncryptionException("Error",e); }
        return encode(output);
    }

    /* ENCODE DE LA INFORMACIÓN */
    private String encode(byte[] output){
        if(mode.equalsIgnoreCase("BASE64"))
            return Base64.encodeBase64String(output); else
            return new String (Hex.encodeHex(output));
    }

    /* DESCIFRAR LA INFORMACIÓN @return String */ public String decrypt(String data){
        byte[] output = null; try{
            byte[] keyBytes = decode(key);
            byte[] input = decode(data);
            SecretKeySpec keySpec = null;
            keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(mode);
            AlgorithmParameterSpec ivSpec= new IvParameterSpec(Hex.decodeHex(iv.toCharArray())); cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            output = cipher.doFinal(input);
        }
        catch(Exception e){
            throw new EncryptionException("Error",e);
        }
        return new String(output);
    }

    /* DECODE DE LA INFORMACIÓN */
    private byte[] decode(String data) throws DecoderException {
        if(data.indexOf("=")>0||data.indexOf("+")>0 )
            return Base64.decodeBase64(data); else
            return Hex.decodeHex(data.toCharArray());
    }


} /* FIN DE LA CLASE */
