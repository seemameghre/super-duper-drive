package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    Credential getCredentialById(Integer credentialid);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> getCredentialsByUserId(Integer userid);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url},#{username},#{key},#{password},#{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insert(Credential credential);

    @Delete("DELETE FROM CREDENTIAL WHERE credentialid = #{credentialid}")
    int delete(Integer credentialid);
}
