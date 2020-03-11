package br.com.calcard.android.app.utils

import android.util.Base64
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class CipherUtil {

    companion object {
        private const val publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqfEwu2cxg8PUmOctzRbjMAFvhXwakxrWky1jAhmkRLmevGxc+b2kay/9/AiaRL3Exw6GL7SXo0Y2Hr1t6ODc6CwYOyVPeVPbx8za2inJSOxqT6GxOcrQuzhxOyF7jlbaAxQK+IEUsHhBKiE4Vd8J52RmJ1rzEYFiori8jbNUIOrM6WhloLdv0XGaVesToP9rD3kCbcRCWS7iXmHuisfMvHhTQLCGt2t0Cotu+1/+vGixDMXvDFbMrK0TWFF+3k+VZwSyBY5IKJdbBbj1ET/M7iocLOLXiCiIPLvRkue7v6ykKsMLNP23XJ/8n0+XXXzx5YjtGDgnpCcxG/PGXBxBawIDAQAB"
        fun encrypt(data: String): String {
            val keyFactory = KeyFactory.getInstance("RSA")
            val pubKey = keyFactory.generatePublic(X509EncodedKeySpec(Base64.decode(publicKey.toByteArray(), Base64.DEFAULT)))
            return try {
                val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
                cipher.init(1, pubKey)
                Base64.encodeToString(cipher.doFinal(data.toByteArray()), Base64.DEFAULT)?.replace("\n", "") ?: ""
            } catch (var2: Exception) {
                throw Exception(var2)
            }
        }
    }
}