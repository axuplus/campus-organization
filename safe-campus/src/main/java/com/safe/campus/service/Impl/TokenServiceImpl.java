package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.safe.campus.about.constant.GlobalConstant;
import com.safe.campus.about.exception.BizException;
import com.safe.campus.about.token.JwtUtil;
import com.safe.campus.about.token.TokenObject;
import com.safe.campus.about.utils.Md5Utils;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.date.DateUtil;
import com.safe.campus.enums.ErrorCodeEnum;
import com.safe.campus.mapper.SchoolTeacherMapper;
import com.safe.campus.mapper.SysAdminUserMapper;
import com.safe.campus.model.domain.SysAdmin;
import com.safe.campus.model.dto.UserTokenDto;
import com.safe.campus.model.vo.LoginTokenVo;
import com.safe.campus.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Transactional(rollbackFor = Exception.class)
public class TokenServiceImpl extends ServiceImpl<SysAdminUserMapper, SysAdmin> implements TokenService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysAdminUserMapper sysAdminUserMapper;

    @Autowired
    private SchoolTeacherMapper teacherMapper;

    @Value(value = "${token.secretKey}")
    private String token_secret_key;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value(value = "${token.ttl}")
    private Integer token_ttl;

    @Override
    public LoginTokenVo token(String account, String pwd) {
        QueryWrapper<SysAdmin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", account);
        SysAdmin sysAdminUser = sysAdminUserMapper.selectOne(queryWrapper);
        if (PublicUtil.isEmpty(sysAdminUser)) {
            logger.info("账户{}不存在", account);
            throw new BizException(ErrorCodeEnum.SYS20000002);
        }
        if (1 == sysAdminUser.getState()) {
            logger.info("账户{}已经被禁用", sysAdminUser.getId());
            throw new BizException(ErrorCodeEnum.SYS20000005);
        }
        if (!Md5Utils.md5Str(pwd).equals(sysAdminUser.getPassword())) {
            logger.info("密码{}错误", account);
            throw new BizException(ErrorCodeEnum.SYS10011006);
        }
        final Date nowDate = new Date();
        final Date expiresIn = DateUtil.addDay(nowDate, token_ttl);
        final JwtUtil.JWTInfo jwtInfo = new JwtUtil().createJWT(nowDate, expiresIn, new TokenObject(token_secret_key, sysAdminUser.getId()));
        UserTokenDto userTokenDto = new UserTokenDto();
        userTokenDto.setUserId(sysAdminUser.getId());
        userTokenDto.setUserName(sysAdminUser.getUserName());
        userTokenDto.setToken(jwtInfo.getToken());
        userTokenDto.setType(sysAdminUser.getType());
        if (3 == sysAdminUser.getType()) {
            userTokenDto.setMasterId(sysAdminUser.getMasterId());
        }
        userTokenDto.setExpireTime(jwtInfo.getExpireIn());
        userTokenDto.setCreateTime(DateUtil.currentTime(nowDate));
        redisTemplate.opsForHash().put(GlobalConstant.LOGIN_TOKEN, String.valueOf(sysAdminUser.getId()), userTokenDto);
        LoginTokenVo loginTokenVo = new LoginTokenVo();
        loginTokenVo.setUserId(sysAdminUser.getId());
        loginTokenVo.setUserName(sysAdminUser.getUserName());
        loginTokenVo.setToken(jwtInfo.getToken());
        loginTokenVo.setExpireIn(jwtInfo.getExpireIn());
        loginTokenVo.setType(sysAdminUser.getType());
        if (3 == sysAdminUser.getType()) {
            loginTokenVo.setMasterId(sysAdminUser.getMasterId());
            loginTokenVo.setType(sysAdminUser.getType());
        }
        return loginTokenVo;
    }

    @Override
    public Boolean exit(Long userId) {
        logger.info("正在退出用户 {}", userId);
        Long delete = redisTemplate.opsForHash().delete(GlobalConstant.LOGIN_TOKEN, String.valueOf(userId));
        logger.info("退成成功 {}", delete);
        return true;
    }
}
