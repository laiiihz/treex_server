package tech.laihz.treex_server.mapper

import org.apache.ibatis.annotations.*
import org.springframework.stereotype.Repository
import tech.laihz.treex_server.entity.User


@Mapper
@Repository
interface UserMapper {
    @Insert("""
        INSERT INTO login_user(name, password)
        VALUES (#{name}, #{password})
    """)
    fun addUser(user: User): Int

    @Update("""
        UPDATE login_user 
        SET password = #{password}
        WHERE name = #{name}
    """)
    fun updatePassword(name: String, password: String)

    @Delete("""
        DELETE FROM login_user
        WHERE name = #{name}
    """)
    fun deleteUserByName(name: String?)

    @Select("""
        SELECT *
        FROM login_user
        WHERE name = #{name}
    """)
    fun getUserByName(name: String): User

    @Update("""
        UPDATE  login_user
        SET phone = #{phone}
        WHERE name = #{name}
    """)
    fun updateUserPhone(name:String,phone:String)

    @Update("""
        UPDATE login_user
        SET email = #{email}
        WHERE name =#{name}
    """)
    fun updateUserEmail(name:String,email:String)

    @Update("""
        UPDATE login_user
        SET avatar = #{avatar}
        WHERE name = #{name}
    """)
    fun updateUserAvatar(name:String,avatar:String)

    @Select("""
        SELECT avatar
        FROM login_user
        WHERE name = #{name}
    """)
    fun getAvatarByName(name:String):String
}