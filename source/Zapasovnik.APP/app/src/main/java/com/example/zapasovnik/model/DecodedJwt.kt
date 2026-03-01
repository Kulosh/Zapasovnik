package com.example.zapasovnik.model

import org.json.JSONObject

data class DecodedJwt(
    var header: JSONObject,

    var payload: JSONObject,

    var signature: String
)
