package qu_Item.common.Exception;

import qu_Item.common.enume.ExceptionEnum;

//通用异常的处理;
public class quException extends RuntimeException {
    private ExceptionEnum exceptionEnum;

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public quException(ExceptionEnum exceptionEnum){
         this.exceptionEnum = exceptionEnum;
    }

}
