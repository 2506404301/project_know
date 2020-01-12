package qu_Item.common.vo;


import qu_Item.common.enume.ExceptionEnum;
//返回结果的对象;
public class ExceptionResult {
    private int status;
    private String message;
    private  Long timetamp;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimetamp() {
        return timetamp;
    }

    public void setTimetamp(Long timetamp) {
        this.timetamp = timetamp;
    }

    public  ExceptionResult(ExceptionEnum em){
        this.message = em.getMsg();
        this.status = em.getCode();
        this.timetamp = System.currentTimeMillis();
    }
}
