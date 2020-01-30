package tech.laihz.treex_server.entity

import lombok.Data

@Data
class User{
    var id: Int = 0
    var name: String = ""
    var password: String = ""
    var phone: String = ""
    var email: String = ""
}