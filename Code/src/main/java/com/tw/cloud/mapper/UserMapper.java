package com.tw.cloud.mapper;

import com.tw.cloud.bean.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMapper {

    User selectUserByLoginName(@Param("loginName") String loginName);

    void updateUserHeader(@Param("loginName") String loginName,@Param("headUrl") String headUrl);

    int insert(User user);
}
