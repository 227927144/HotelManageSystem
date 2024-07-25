package hotel.controller.admin;

import cn.hutool.crypto.SecureUtil;
import hotel.common.CommonResult;
import hotel.common.StatusCode;
import hotel.domain.Admin;
import hotel.dto.AdminLoginDTO;
import hotel.service.AdminService;
import hotel.util.WebUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author：rabbiter
 * @date：2023/01/01 19:31
 * Description：
 */
@RestController("adminAdminController")
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @PostMapping(value = "/login")
    public CommonResult<Admin> login(@RequestBody AdminLoginDTO adminLoginDTO) {
        CommonResult<Admin> commonResult = new CommonResult<>();
        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq("admin_name", adminLoginDTO.getUserName());
        String md5Password = SecureUtil.md5(adminLoginDTO.getPassword());
        queryWrapper.eq("password", md5Password);
        Admin admin = adminService.getOne(queryWrapper);

        if (null != admin) {
            admin.setAdmin_name(adminLoginDTO.getUserName());
            WebUtils.getSession().setAttribute("loginAdmin", admin);

            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData(admin);
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage("账号或密码错误");
            commonResult.setData(null);
        }

        return commonResult;
    }

    @GetMapping("/logout")
    public CommonResult<String> logout(){
        CommonResult<String> commonResult = new CommonResult<>();

        WebUtils.getSession().removeAttribute("loginAdmin");

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData("登出成功!");

        return commonResult;
    }
}
