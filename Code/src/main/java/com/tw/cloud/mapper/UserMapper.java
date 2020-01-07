package com.tw.cloud.mapper;

import com.tw.cloud.bean.User;
import com.tw.cloud.bean.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    com.tw.cloud.bean.user.User selectUserByLoginName(@Param("loginName") String loginName);

    void updateUserHeader(@Param("loginName") String loginName,@Param("headUrl") String headUrl);

    int insert(com.tw.cloud.bean.user.User user);
}