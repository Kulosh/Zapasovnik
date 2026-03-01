package com.example.zapasovnik

import com.example.zapasovnik.model.DecodedJwt
import org.json.JSONObject
import java.util.Base64

class JwtDecoder {
    companion object {
        fun decodeJwtWithoutVerification(token: String): DecodedJwt {
            val parts = token.split(".")
            require(parts.size == 3) { "Invalid JWT format" }

            fun b64UrlToJson(part: String): JSONObject {
                val decoded = Base64.getUrlDecoder().decode(part)
                return JSONObject(String(decoded, Charsets.UTF_8))
            }

            return DecodedJwt(
                header = b64UrlToJson(parts[0]),
                payload = b64UrlToJson(parts[1]),
                signature = parts[2]
            )
        }
    }
}