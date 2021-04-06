package ke.co.payment.integrations;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import static java.nio.charset.StandardCharsets.UTF_8;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class InitiatorPasswordEncryption
{
    /**
     * Encrypts plaintext password and returns encrypted password. No more changes are required to be done on
     * the final text
     *
     * @return
     * @throws Exception
     */
    public static String encryptInitiatorPassword(String securityCertificate, String password) {
        String encryptedPassword = "";
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            byte[] input = password.getBytes();

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            FileInputStream fin = new FileInputStream(new File(securityCertificate));
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(fin);
            PublicKey pk = certificate.getPublicKey();
            cipher.init(Cipher.ENCRYPT_MODE, pk);

            byte[] cipherText = cipher.doFinal(input);

            // Convert the resulting encrypted byte array into a string using base64 encoding
            encryptedPassword = Base64.encode(cipherText);

        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }  catch (NoSuchPaddingException ex) {
            ex.printStackTrace();
            //Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
          //  Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            ex.printStackTrace();
           // Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
           // Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();
            //Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
            //Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        return encryptedPassword;
    }


    /**
     * The Public key certificate being used. Replace with the respective certificate required depending on environment.
     * To get it, just open the certificate in notepad++ and copy paste the contents here, then remove the
     * new line characters '\n' <strong>EXCEPT</strong> the first and last newlines. Check the one below for the required format
     *
     * This is currently the G2 sandbox certificate, will not work with Daraja sandbox
     *
     * @return certificate in String format
     */
    private String getCertificate()
    {
        String certStr = "-----BEGIN CERTIFICATE-----\n" +
                "MIIGKzCCBROgAwIBAgIQDL7NH8cxSdUpl0ihH0A1wTANBgkqhkiG9w0BAQsFADBN\n" +
                "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMScwJQYDVQQDEx5E\n" +
                "aWdpQ2VydCBTSEEyIFNlY3VyZSBTZXJ2ZXIgQ0EwHhcNMTgwODI3MDAwMDAwWhcN\n" +
                "MTkwNDA0MTIwMDAwWjBuMQswCQYDVQQGEwJLRTEQMA4GA1UEBxMHTmFpcm9iaTEW\n" +
                "MBQGA1UEChMNU2FmYXJpY29tIFBMQzETMBEGA1UECxMKRGlnaXRhbCBJVDEgMB4G\n" +
                "A1UEAxMXc2FuZGJveC5zYWZhcmljb20uY28ua2UwggEiMA0GCSqGSIb3DQEBAQUA\n" +
                "A4IBDwAwggEKAoIBAQC78yeC/wLoZY6TJeqc4g/9eAKIpeCwEsjX09pD8ZxAGXqT\n" +
                "Oi7ssdIGJBPmJZNeEVyf8ocFhisCuLngJ9Z5e/AvH52PhrEFmVu2D03zSf4C+rhZ\n" +
                "ndEKP6G79pUAb/bemOliU9zM8xYYkpCRzPWUzk6zSDarg0ZDLw5FrtZj/VJ9YEDL\n" +
                "WGgAfwExEgSN3wjyUlJ2UwI3wqQXLka0VNFWoZxUH5j436gbSWRIL6NJUmrq8V8S\n" +
                "aTEPz3eJHj3NOToDu245c7VKdF/KExyZjRjD2p5I+Aip80TXzKlZj6DjMb3DlfXF\n" +
                "Hsnu0+1uJE701mvKX7BiscxKr8tCRphL63as4dqvAgMBAAGjggLkMIIC4DAfBgNV\n" +
                "HSMEGDAWgBQPgGEcgjFh1S8o541GOLQs4cbZ4jAdBgNVHQ4EFgQUzZmY7ZORLw9w\n" +
                "qRbAQN5m9lJ28qMwIgYDVR0RBBswGYIXc2FuZGJveC5zYWZhcmljb20uY28ua2Uw\n" +
                "DgYDVR0PAQH/BAQDAgWgMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjBr\n" +
                "BgNVHR8EZDBiMC+gLaArhilodHRwOi8vY3JsMy5kaWdpY2VydC5jb20vc3NjYS1z\n" +
                "aGEyLWc2LmNybDAvoC2gK4YpaHR0cDovL2NybDQuZGlnaWNlcnQuY29tL3NzY2Et\n" +
                "c2hhMi1nNi5jcmwwTAYDVR0gBEUwQzA3BglghkgBhv1sAQEwKjAoBggrBgEFBQcC\n" +
                "ARYcaHR0cHM6Ly93d3cuZGlnaWNlcnQuY29tL0NQUzAIBgZngQwBAgIwfAYIKwYB\n" +
                "BQUHAQEEcDBuMCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5kaWdpY2VydC5jb20w\n" +
                "RgYIKwYBBQUHMAKGOmh0dHA6Ly9jYWNlcnRzLmRpZ2ljZXJ0LmNvbS9EaWdpQ2Vy\n" +
                "dFNIQTJTZWN1cmVTZXJ2ZXJDQS5jcnQwCQYDVR0TBAIwADCCAQUGCisGAQQB1nkC\n" +
                "BAIEgfYEgfMA8QB2AKS5CZC0GFgUh7sTosxncAo8NZgE+RvfuON3zQ7IDdwQAAAB\n" +
                "ZXs1FvEAAAQDAEcwRQIgBzVMkm7SNprjJ1GBqiXIc9rNzY+y7gt6s/O02oMkyFoC\n" +
                "IQDBuThGlpmUKpeZoHhK6HGwB4jDMIecmKaOcMS18R2jxwB3AId1v+dZfPiMQ5lf\n" +
                "vfNu/1aNR1Y2/0q1YMG06v9eoIMPAAABZXs1F8IAAAQDAEgwRgIhAIRq2XFiC+RS\n" +
                "uDCYq8ICJg0QafSV+e9BLpJnElEdaSjiAiEAyiiW4vxwv4cWcAXE6FAipctyUBs6\n" +
                "bE5QyaCnmNpoDiQwDQYJKoZIhvcNAQELBQADggEBAB0YoWve9Sxhb0PBS3Hc46Rf\n" +
                "a7H1jhHuwE+UyscSQsdJdk8uPAgDuKRZMvJPGEaCkNHm36NfcaXXFjPOl7LI1d1a\n" +
                "9zqSP0xeZBI6cF0x96WuQGrI9/WR2tfxjmaUSp8a/aJ6n+tZA28eJZNPrIaMm+6j\n" +
                "gh7AkKnqcf+g8F/MvCCVdNAiVMdz6UpCscf6BRPHNZ5ifvChGh7aUKjrVLLuF4Ls\n" +
                "HE05qm6HNyV5eTa6wvcbc4ewguN1UDZvPWetSyfBk10Wbpor4znQ4TJ3Y9uCvsJH\n" +
                "41ldblDvZZ2z4kB2UYQ7iBkPlJSxSOaFgW/GGDXq49sz/995xzhVITHxh2SdLkI=\n" +
                "-----END CERTIFICATE-----";

        return certStr;
    }
}
