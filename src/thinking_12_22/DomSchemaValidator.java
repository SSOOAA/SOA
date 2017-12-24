package thinking_12_22;

import assignment4.SchemaValidator;

public class DomSchemaValidator {
    public static void main(String[] args){
        assignment4.SchemaValidator schemaValidator=new SchemaValidator();
        String xsdPath="src/thinking_12_22/StudentList.xsd";
        String xmlPath="src/assignment4/StudentList.xml";
        System.out.print(thinking_12_22.SchemaValidator.validateSchema(xsdPath,xmlPath));
    }
}
