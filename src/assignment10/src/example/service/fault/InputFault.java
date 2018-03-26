package example.service.fault;

import javax.xml.ws.WebFault;

@WebFault(name = "inputFault", targetNamespace = "http://jw.nju.edu.cn/schema")
public class InputFault extends Exception{

    private InputFaultType faultType;

    public InputFault(InputFaultType faultType){
        super("错误或无效的输入");
        this.faultType = faultType;
    }

}
