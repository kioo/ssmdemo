package com.ssm.demo.controller;

import com.ssm.demo.common.Constants;
import com.ssm.demo.common.Result;
import com.ssm.demo.common.ResultGenerator;
import com.ssm.demo.controller.annotation.TokenToUser;
import com.ssm.demo.entity.AdminUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testLogin")
public class TestLoginController {

    @RequestMapping(value="/test1", method = RequestMethod.GET)
    public Result test1(@TokenToUser AdminUser user){
        Result result = null;
        if(user == null){
            result = ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN,"未登录！");
        }else{
            result = ResultGenerator.genSuccessResult("登录验证通过");
        }
        return result;
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public Result test2() {
        //此接口不含@TokenToUser注解，即访问此接口无需登陆验证，此类接口在实际开发中应该很少，为了安全起见应该所有接口都会做登陆验证。
        Result result = ResultGenerator.genSuccessResult("此接口无需登陆验证，请求成功");
        //直接返回业务逻辑返回的数据即可。
        return result;
    }
}
