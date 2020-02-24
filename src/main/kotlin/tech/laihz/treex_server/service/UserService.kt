package tech.laihz.treex_server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.laihz.treex_server.entity.User
import tech.laihz.treex_server.mapper.UserMapper

@Service
class UserService {
    @Autowired
    lateinit var userMapper: UserMapper

    fun addUser(user: User): Int {
        return userMapper.addUser(user)
    }

    fun deleteUserByName(name: String?) {
        return userMapper.deleteUserByName(name)
    }

    fun getUserByName(name: String): User {
        return userMapper.getUserByName(name)
    }

    fun updateUserPassword(name: String, password: String) {
        return userMapper.updatePassword(name, password)
    }

    fun updateUserPhone(name: String, phone: String) {
        return userMapper.updateUserPhone(name, phone)
    }

    fun updateUserEmail(name: String, email: String) {
        return userMapper.updateUserEmail(name, email)
    }

    fun updateUserAvatar(name: String, avatar: String) {
        return userMapper.updateUserAvatar(name, avatar)
    }

    fun getAvatarByName(name: String):String {
        return userMapper.getAvatarByName(name)
    }

}