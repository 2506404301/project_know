package qu_Item.user.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qu_Item.unity.User;
import qu_Item.user.mapper.UserMapper;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;



    public Boolean checkData(String data, Integer type) {
        //数据的唯一校验
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new quException(ExceptionEnum.ERROR_USER_DATA_TYPE);
        }
        int count = userMapper.selectCount(user);
        return count == 0;
    }


    /**
     * 注册功能;
     * @param user
     */
    public void register(User user) {
        //对密码加密
        String md5Hex = DigestUtils.md5Hex("1212" + DigestUtils.md5Hex(user.getPassword()) + "opop");
        //注册如数据库;
        user.setPassword(md5Hex);
        user.setCreated(new Date());
        userMapper.insert(user);
    }

    /**
     * 根据用户名和密码进行查询;
     *
     * @param username
     * @param password
     * @return
     */
    public User queryUserByNameAndPassword(String username, String password) {
        //查询
        User user = new User();
        user.setUsername(username);
        User one = userMapper.selectOne(user);
        //校验用户名称
        if (one == null){
            throw new quException(ExceptionEnum.ERROR_USERNAME_PASSWORD);
        }
        //校验密码
        if (!StringUtils.equals(one.getPassword(),DigestUtils.md5Hex("1212" + DigestUtils.md5Hex(password) + "opop"))){
            throw new quException(ExceptionEnum.ERROR_USERNAME_PASSWORD);
        }
        return one;
    }
}
