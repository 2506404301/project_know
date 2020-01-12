package qu_Item.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qu_Item.unity.User;
import qu_Item.user.service.UserService;
import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 参数的校验;
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(
            @PathVariable("data") String data,@PathVariable("type") Integer type){
        return ResponseEntity.ok(userService.checkData(data,type));
    }


    /**
     * 祖册功能
     * @param user
     * @return
     * BindingResult 手机异常但不处理
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, BindingResult result){
        /*这里就是直接通过浏览器流氓性的注册，但是没有做有好的异常处理*/
        if (result.hasErrors()){
            throw new RuntimeException(result.getAllErrors().stream()
                    .map(e->e.getDefaultMessage()).collect(Collectors.joining("|")));
        }
        userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据找好和密码查询;
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUserByUserNameAndPassword(@RequestParam("username") String username,
                                                               @RequestParam("password")String password){

        return ResponseEntity.ok(userService.queryUserByNameAndPassword(username,password));
     }



}
