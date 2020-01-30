package tech.laihz.treex_server.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Repository
import tech.laihz.treex_server.entity.User


@Mapper
@Repository
interface UserMapper {
    @Insert("""
        INSERT INTO login_user(name, password)
        VALUES (#{name}, #{password})
    """)
    fun addUser(user:User):Int

    @Select("""
        SELECT *
        FROM login_user
        WHERE name = #{name}
    """)
    fun getUserByName(name: String): User

}