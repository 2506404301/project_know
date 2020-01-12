package qu_Item.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaUtils {
        /**
         * 从文件中读取公钥
         *
         * @param filename 公钥保存路径，相对于classpath
         * @return 公钥对象
         * @throws Exception
         */
        public static PublicKey getPublicKey(String filename) throws Exception {
            System.out.println("filename = " + filename);
            byte[] bytes = readFile(filename);
            return getPublicKey(bytes);
        }

    /**
     * 从文件中读取密钥
     *
     * @param filename 私钥保存路径，相对于classpath
     * @return 私钥对象
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        System.out.println("filename = " + filename);
        byte[] bytes = readFile(filename);
        return getPrivateKey(bytes);
    }


        /**
         * 获取公钥
         *
         * @param bytes 公钥的字节形式
         * @return
         * @throws Exception
         */
        public static PublicKey getPublicKey(byte[] bytes) throws Exception {
            //构造X509EncodedKeySpec对象;
            X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
            //指定rsa加密算法;
            KeyFactory factory = KeyFactory.getInstance("RSA");
            //生成公钥密文
            return factory.generatePublic(spec);
        }


        /**
         * 获取密钥
         *
         * @param bytes 私钥的字节形式
         * @return
         * @throws Exception
         */
        public static PrivateKey getPrivateKey(byte[] bytes) throws Exception {
            //构造PKCS8EncodedKeySpec对象;
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
            //指定rsa机密方法;
            KeyFactory factory = KeyFactory.getInstance("RSA");
            //生成指定的密文;
            System.out.println("factory.generatePublic(spec) = " + factory.generatePrivate(spec));
            return factory.generatePrivate(spec);
        }

    /**
     * 根据密文，生成rsa公钥和私钥,并写入指定文件
     *
     * @param publicKeyFilename  公钥文件路径
     * @param privateKeyFilename 私钥文件路径
     * @param secret             生成密钥的密文
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String secret) throws Exception {
        //用于生成公钥和私钥对KeyPairGenerator
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //根据秘钥生成一定的随机秘钥文
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        //利用上面的随机数据源初始化这个KeyPairGenerator对象
        keyPairGenerator.initialize(1024, secureRandom);
        //拿到的秘钥
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        // 获取公钥并写出
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        writeFile(publicKeyFilename, publicKeyBytes);
        // 获取私钥并写出
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        writeFile(privateKeyFilename, privateKeyBytes);
    }

        /**
         * 根据文件的名称读取文件,返回字符数组
         *
         * @param fileName
         * @return
         * @throws Exception
         */
        private static byte[] readFile(String fileName) throws Exception {
                return Files.readAllBytes(new File(fileName).toPath());
            }

        /**
         * 根据路径和字符数组写入文件
         *
         * @param destPath
         * @param bytes
         * @throws IOException
         */
        private static void writeFile(String destPath, byte[] bytes) throws IOException {
                File dest = new File(destPath);
                if (!dest.exists()) {
                    dest.createNewFile();
                }
                Files.write(dest.toPath(), bytes);
            }
    }

