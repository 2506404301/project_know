package qu_Item.common.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import qu_Item.common.Exception.quException;
import qu_Item.common.vo.ExceptionResult;

//默认情况下controller的类;
//也就是显示页面的信息;
@ControllerAdvice
public class commonExceptionHandler {
    //指定了异常类;
    @ExceptionHandler(quException.class)
    public ResponseEntity<ExceptionResult> handlerException(quException e){
        return  ResponseEntity.status(e.getExceptionEnum().getCode())
                .body(new ExceptionResult(e.getExceptionEnum()));
    }
}
