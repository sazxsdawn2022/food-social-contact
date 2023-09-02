package com.imooc.oauth2.server.service;

import com.imooc.commons.model.domain.SignInIdentity;
import com.imooc.commons.model.pojo.Diners;
import com.imooc.commons.utils.AssertUtil;
import com.imooc.oauth2.server.mapper.DinersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录校验
 * 实现这个接口，然后重写一个方法
 */
@Service
public class UserService implements UserDetailsService {

    @Resource
    private DinersMapper dinersMapper;

    //用户登录时肯定有用户名和密码，这里只拿用户名，数据库中有完整的信息，密码的校验由Security帮助做，如果密码校验通过了会给用户生成令牌
    //返回的实际是Security的user对象
    //生成令牌后把令牌存到redis中
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AssertUtil.isNotEmpty(username, "请输入用户名");
        //从数据库中查出dinner
        Diners diners = dinersMapper.selectByAccountInfo(username);
        if (diners == null) {
            // security自己提供的异常
            throw new UsernameNotFoundException("用户名或密码错误，请重新输入");
        }
//        return new User(username, diners.getPassword(),
//                AuthorityUtils.commaSeparatedStringToAuthorityList(diners.getRoles()));

        // 初始化登录认证对象
        SignInIdentity signInIdentity = new SignInIdentity();
        // 拷贝属性
        BeanUtils.copyProperties(diners, signInIdentity);
        return signInIdentity;
    }

}
