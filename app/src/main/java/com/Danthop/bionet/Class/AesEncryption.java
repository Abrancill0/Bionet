package com.serti.pandora.crypto.symmetric.impl;

import com.serti.pandora.crypto.EncryptionException;

import java.io.File;
import java.io.FileInputStream;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;



/**
 * This class is the AES implementation for symmetric encryption.
 * The available paramters for this implementation are:
 * @param AES.KEY  : the key in ascii format. 128 bits
 * @param AES.IV   : the initialization vector in hex format. 16 bytes
 * @param AES.MODE : the cipher block mode to use and padding like :  CBC ( cipher block chain ), ECB ( electronic code block ). ex: AES/CBC/PKCS7Padding
 * @author Rotten
 *
 */
public class AesEncryption
{
    Map<String,String> initParams;

    String key;
    String iv;
    String mode;
    String encoding;
    String keyFile;

    //	String ENCODING = "ISO-8859-1";
    String ENCODING = "UTF-8";
    public AesEncryption()
    {
        Security.addProvider( new BouncyCastleProvider( ) );
    }

    /**
     *
     */
    public void setInitParams(Map<String, String> params)
    {
        initParams=params;
        key=params.get("AES.KEY");
        iv=params.get("AES.IV");
        mode=params.get("AES.MODE");
        encoding=params.get("AES.ENCODING");
        keyFile=params.get("AES.KEYFILE");

        //keyFile can be null
        //if keyFile is not null, overwrite the value of the key and IV
        if(keyFile!=null)
        {
            File f = new File(keyFile);
            if(!f.exists())
                throw new NullPointerException("AES.KEYFILE does not exists.");
            Properties p;
            try
            {
                p = new Properties();
                p.load(new FileInputStream(f));
            }
            catch(Exception e)
            {
                throw new IllegalArgumentException("Could not load Key and IV values");
            }

            //the file parameters overwrite the others

            iv=p.getProperty("AES.IV");
            key=p.getProperty("AES.KEY");

        }

        if(iv==null)
            throw new NullPointerException("AES.IV cannot be null");

        if(iv.length()<32)
            throw new IllegalArgumentException("AES.IV cannot be null");

        if(mode==null)
            throw new NullPointerException("AES.MODE cannot be null");

        if(encoding==null)
            throw new NullPointerException("AES.ENCODING cannot be null");

        if(encoding.equalsIgnoreCase("BASE64")&&encoding.equalsIgnoreCase("HEX"))
            throw new IllegalArgumentException("AES.ENCODING can only be 'HEX' of 'BASE64'");



    }

    /**
     * This method encrypts the data with the previously specified key
     * @return encodeBase24 **/
    public String encrypt(String data)
    {
        byte[] output = null;
        try
        {

            byte[] keyBytes = decode(key);
            byte[] input = data.getBytes(ENCODING);


            AlgorithmParameterSpec ivSpec = new IvParameterSpec(Hex.decodeHex(iv.toCharArray()));
            SecretKeySpec keySpec = null;
            keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(mode);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            output = cipher.doFinal(input);
        }
        catch(Exception e)
        {
            throw new EncryptionException("Error",e);
        }
        return encode(output);
    }

    private String encode(byte[] output)
    {
        if(mode.equalsIgnoreCase("BASE64"))
            return Base64.encodeBase64String(output);
        else // HEX
        {
            return new String (Hex.encodeHex(output)); //changed method to this to ensure compatibility with commons-codec 1.3+
        }
    }

    private byte[] decode(String data) throws DecoderException
    {
        if(data.indexOf("=")>0||data.indexOf("+")>0 )
            return  Base64.decodeBase64(data);
        else
            return Hex.decodeHex(data.toCharArray());
    }



}
