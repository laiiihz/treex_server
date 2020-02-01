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

}