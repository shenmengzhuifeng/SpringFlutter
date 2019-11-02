package tw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tw.bean.CommonResp;
import tw.service.UserService;
import tw.utils.Constants;
import tw.utils.UnifyApiUri;

/**
 * 用户相关controller
 *
 * @author
 * @create 2019-10-20 7:50 PM
 **/
@RestController
@RequestMapping(UnifyApiUri.UserApi.API_CUSTOMER_BASE)
public class UserController {

    @Autowired
    private UserService mUserService;

    @RequestMapping(value = UnifyApiUri.UserApi.API_CUSTOMER_INFO,method = RequestMethod.GET)
    public CommonResp<String> getCustomerInfo() {
        return new CommonResp<>(Constants.RESULT_OK,"","","获取成功");
    }
}
